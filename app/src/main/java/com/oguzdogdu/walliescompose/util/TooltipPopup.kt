import android.view.View
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import kotlin.math.roundToInt

@Composable
fun TooltipPopup(
    modifier: Modifier = Modifier,
    triggeredView: @Composable (Modifier) -> Unit,
    tooltipContent: @Composable () -> Unit,
) {
    var isShowTooltip by remember { mutableStateOf(false) }
    var position by remember { mutableStateOf(TooltipPopupPosition()) }

    val view = LocalView.current.rootView

    if (isShowTooltip) {
        TooltipPopup(
            onDismissRequest = {
                isShowTooltip = isShowTooltip.not()
            },
            position = position,
        ) {
            tooltipContent()
        }
    }
    triggeredView(modifier
        .clickableTooltip {
            isShowTooltip = isShowTooltip.not()
        }
        .onGloballyPositioned { coordinates ->
            position = calculateTooltipPopupPosition(view, coordinates)
        }
    )
}

@Composable
fun TooltipPopup(
    position: TooltipPopupPosition,
    backgroundShape: Shape = MaterialTheme.shapes.medium,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    arrowHeight: Dp = 8.dp,
    horizontalPadding: Dp = 16.dp,
    onDismissRequest: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    var alignment = Alignment.TopCenter
    var offset = position.offset

    val horizontalPaddingInPx = with(LocalDensity.current) { horizontalPadding.toPx() }
    var arrowPositionX by remember { mutableFloatStateOf(position.centerPositionX) }

    with(LocalDensity.current) {
        val arrowPaddingPx = arrowHeight.toPx().roundToInt() * 2

        when (position.alignment) {
            TooltipAlignment.TopCenter -> {
                alignment = Alignment.TopCenter
                offset = offset.copy(
                    y = position.offset.y + arrowPaddingPx
                )
            }
            TooltipAlignment.BottomCenter -> {
                alignment = Alignment.BottomCenter
                offset = offset.copy(
                    y = position.offset.y - arrowPaddingPx
                )
            }
        }
    }

    val popupPositionProvider = remember(alignment, offset) {
        mutableStateOf(TooltipAlignmentOffsetPositionProvider(
            alignment = alignment,
            offset = offset,
            horizontalPaddingInPx = horizontalPaddingInPx,
            centerPositionX = position.centerPositionX,
        ) { position ->
            arrowPositionX = position
        })
    }

    Popup(
        popupPositionProvider = popupPositionProvider.value,
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(dismissOnBackPress = false),
    ) {
        TooltipMainLayout(
            alignment = position.alignment,
            arrowHeight = arrowHeight,
            arrowPositionX = arrowPositionX,
            modifier = Modifier
                .padding(horizontal = horizontalPadding)
                .background(
                    color = backgroundColor,
                    shape = backgroundShape,
                )
        ) {
            content()
        }
    }
}

@Stable
internal class TooltipAlignmentOffsetPositionProvider(
    val alignment: Alignment,
    val offset: IntOffset,
    val centerPositionX: Float,
    val horizontalPaddingInPx: Float,
    private val onArrowPositionX: (Float) -> Unit,
) : PopupPositionProvider {

    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        var popupPosition = IntOffset(0, 0)

        val parentAlignmentPoint = alignment.align(
            IntSize.Zero,
            IntSize(anchorBounds.width, anchorBounds.height),
            layoutDirection
        )

        val relativePopupPos = alignment.align(
            IntSize.Zero,
            IntSize(popupContentSize.width, popupContentSize.height),
            layoutDirection
        )

        popupPosition += IntOffset(anchorBounds.left, anchorBounds.top)

        popupPosition += parentAlignmentPoint

        popupPosition -= IntOffset(relativePopupPos.x, relativePopupPos.y)

        val resolvedOffset = IntOffset(
            offset.x * (if (layoutDirection == LayoutDirection.Ltr) 1 else -1),
            offset.y
        )

        popupPosition += resolvedOffset

        val leftSpace = centerPositionX - horizontalPaddingInPx
        val rightSpace = windowSize.width - centerPositionX - horizontalPaddingInPx

        val tooltipWidth = popupContentSize.width
        val halfPopupContentSize = popupContentSize.center.x

        val fullPadding = horizontalPaddingInPx * 2

        val maxTooltipSize = windowSize.width - fullPadding

        val isCentralPositionTooltip = halfPopupContentSize <= leftSpace && halfPopupContentSize <= rightSpace

        when {
            isCentralPositionTooltip -> {
                popupPosition = IntOffset(centerPositionX.toInt() - halfPopupContentSize, popupPosition.y)
                val arrowPosition = halfPopupContentSize.toFloat() - horizontalPaddingInPx
                onArrowPositionX.invoke(arrowPosition)
            }
            tooltipWidth >= maxTooltipSize -> {
                popupPosition = IntOffset(windowSize.center.x - halfPopupContentSize, popupPosition.y)
                val arrowPosition = centerPositionX - popupPosition.x - horizontalPaddingInPx
                onArrowPositionX.invoke(arrowPosition)
            }
            halfPopupContentSize > rightSpace -> {
                popupPosition = IntOffset(centerPositionX.toInt(), popupPosition.y)
                val arrowPosition = halfPopupContentSize + (halfPopupContentSize - rightSpace) - fullPadding

                onArrowPositionX.invoke(arrowPosition)
            }
            halfPopupContentSize > leftSpace -> {
                popupPosition = IntOffset(0, popupPosition.y)
                val arrowPosition = centerPositionX - horizontalPaddingInPx
                onArrowPositionX.invoke(arrowPosition)
            }
            else -> {
                val position = centerPositionX
                onArrowPositionX.invoke(position)
            }
        }

        return popupPosition
    }
}

@Composable
fun TooltipMainLayout(
    modifier: Modifier = Modifier,
    alignment: TooltipAlignment = TooltipAlignment.TopCenter,
    arrowHeight: Dp,
    arrowPositionX: Float,
    content: @Composable () -> Unit
) {

    val arrowHeightPx = with(LocalDensity.current) {
        arrowHeight.toPx()
    }
    val arrowColor = MaterialTheme.colorScheme.surfaceVariant
    val animatedArrowPositionX by animateFloatAsState(
        targetValue = arrowPositionX,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessVeryLow
        ), label = ""
    )

    Box(
        modifier = modifier
            .drawBehind {
                if (animatedArrowPositionX <= 0f) return@drawBehind

                val isTopCenter = alignment == TooltipAlignment.TopCenter

                val path = Path()

                if (isTopCenter) {
                    val position = Offset(animatedArrowPositionX, 0f)
                    path.apply {
                        moveTo(x = position.x, y = position.y)
                        lineTo(x = position.x - arrowHeightPx, y = position.y)
                        lineTo(x = position.x, y = position.y - arrowHeightPx)
                        lineTo(x = position.x + arrowHeightPx, y = position.y)
                        lineTo(x = position.x, y = position.y)
                    }
                } else {
                    val arrowY = drawContext.size.height
                    val position = Offset(animatedArrowPositionX, arrowY)
                    path.apply {
                        moveTo(x = position.x, y = position.y)
                        lineTo(x = position.x + arrowHeightPx, y = position.y)
                        lineTo(x = position.x, y = position.y + arrowHeightPx)
                        lineTo(x = position.x - arrowHeightPx, y = position.y)
                        lineTo(x = position.x, y = position.y)
                    }
                }

                drawPath(
                    path = path,
                    color = arrowColor,
                )
                path.close()
            }

    ) {
        AnimatedContent(
            targetState = arrowPositionX,
            transitionSpec = {
                slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.End, initialOffset = {
                    it / 5
                }, animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessVeryLow
                )).togetherWith(
                    ExitTransition.None
                )
            },
            label = "",
        ) { targetValue ->
            if (targetValue > 30f) {
                content()
            }
        }
    }
}

@Stable
data class TooltipPopupPosition(val alignment: TooltipAlignment = TooltipAlignment.TopCenter,
    val offset: IntOffset = IntOffset(0, 0),
    val centerPositionX: Float = 0f,
)

enum class TooltipAlignment {
    TopCenter, BottomCenter
}

@Composable
fun Modifier.clickableTooltip(onClick: () -> Unit): Modifier = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
    )
}

fun calculateTooltipPopupPosition(
    view: View,
    coordinates: LayoutCoordinates,
): TooltipPopupPosition {
    val layoutPosition = IntArray(2)
    view.getLocationOnScreen(layoutPosition)

    val location = coordinates.positionInWindow()

    val x = location.x + coordinates.size.width / 2f
    val y = location.y - layoutPosition[1]

    return if (y < view.height / 2) {
        TooltipPopupPosition(
            alignment = TooltipAlignment.TopCenter,
            offset = IntOffset(0, coordinates.size.height),
            centerPositionX = x
        )
    } else {
        TooltipPopupPosition(
            alignment = TooltipAlignment.BottomCenter,
            offset = IntOffset(0, -coordinates.size.height),
            centerPositionX = x
        )
    }
}