package com.oguzdogdu.walliescompose.features.appstate

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.ui.theme.medium
import kotlinx.coroutines.launch
import kotlin.math.min

@Stable
data class SnackbarModel(
    val type: MessageType = MessageType.SUCCESS,
    @DrawableRes val drawableRes: Int?,
    val message: MessageContent? = null,
    val duration: Duration = Duration.SHORT,
)

@Immutable
enum class MessageType {
    ERROR,
    SUCCESS
}

@Immutable
enum class Duration(val duration: Long) {
    SHORT(4000L),
    LONG(10000L)
}

@Stable
sealed class MessageContent {
    data class ResourceString(@StringRes val resId: Int) : MessageContent()
    data class PlainString(val text: String) : MessageContent()
}

@Composable
fun CustomSnackbar(
    snackbarModel: SnackbarModel?,
    onDismiss: suspend () -> Unit,
    modifier: Modifier = Modifier,
) {
    val errorColor = colorResource(R.color.red)
    val successColor = colorResource(R.color.lush_green)
    var snackbarBackgroundColor by remember { mutableStateOf<Color?>(null) }
    var foldProgress by remember { mutableFloatStateOf(0f) }
    val message = when (val messageContent = snackbarModel?.message) {
        is MessageContent.ResourceString -> stringResource(messageContent.resId)
        is MessageContent.PlainString -> messageContent.text
        null -> ""
    }
    LaunchedEffect(snackbarModel) {
        if (snackbarModel != null) {
            launch {
                snackbarBackgroundColor = when (snackbarModel.type) {
                    MessageType.ERROR -> errorColor
                    MessageType.SUCCESS -> successColor
                }
            }
            launch {
                snackbarModel.let {
                    kotlinx.coroutines.delay(it.duration.duration)
                    animate(
                            1f,
                            0f,
                            animationSpec = tween(1000, easing = EaseOutBounce)
                        ) { value, _ ->
                            foldProgress = value
                        }

                    onDismiss()
                }
            }
            launch {
                animate(
                    0f,
                    1f,
                    animationSpec = tween(1000, easing = EaseOutBounce)
                ) { value, _ ->
                    foldProgress = value
                }
            }
        }
    }

        Box(
            modifier = Modifier
                .padding(16.dp)
                .graphicsLayer {
                    rotationX = 90f * (1f - foldProgress)
                    transformOrigin = TransformOrigin(0.5f, 1f)
                    cameraDistance = 8 * density
                }
        ) {
            Snackbar(
                modifier = modifier.graphicsLayer {
                    alpha = min(1f, foldProgress * 2f)
                },
                containerColor = snackbarBackgroundColor ?: Color.Transparent,
                shape = RoundedCornerShape(12.dp),
                actionOnNewLine = false,
                content = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        if (snackbarModel?.drawableRes != null) {
                            Icon(
                                painter = painterResource(id = snackbarModel.drawableRes),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        if (snackbarModel?.message != null) {
                            Text(
                                text = message ,
                                color = Color.White,
                                fontSize = 14.sp,
                                maxLines = 3,
                                fontFamily = medium,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Start
                            )
                        }
                    }
                }
            )
        }
    }