package com.oguzdogdu.walliescompose.features.detail.component

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.data.common.ImageLoadingState
import com.oguzdogdu.walliescompose.domain.model.favorites.FavoriteImages
import com.oguzdogdu.walliescompose.ui.theme.regular
import kotlinx.coroutines.delay

@Composable
fun QuickFavoriteTransitionCard(
    favoriteImages: List<FavoriteImages>,
    onClickGoToFavorites: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ), shape = RoundedCornerShape(
            16.dp
        ), modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 96.dp, max = 192.dp)
            .padding(bottom = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            UnderlineTextWithAnimation(onClickGoToFavorites = onClickGoToFavorites)
            QuickFavoriteList(favoriteImages = favoriteImages)
        }
    }
}
@Composable
fun UnderlineTextWithAnimation(onClickGoToFavorites: () -> Unit) {
    val underlineColor = MaterialTheme.colorScheme.secondaryContainer
    val firstColorBeforeAnimated = MaterialTheme.colorScheme.onBackground
    val textWidth = remember { mutableFloatStateOf(0f) }
    val textString = stringResource(id = R.string.text_detail_favorite_info)
    val targetWord = remember {
        derivedStateOf {
            when {
                textString.contains("buraya") -> {
                    "buraya"
                }
                else -> {
                    "here"
                }
            }
        }
    }
    val startIndex = remember { mutableIntStateOf(textString.indexOf(string = targetWord.value)) }
    val endIndex = remember { mutableIntStateOf(startIndex.intValue + targetWord.value.length) }
    var onDraw: DrawScope.() -> Unit by remember { mutableStateOf({}) }
    val animatableColorHighlight = remember { Animatable(firstColorBeforeAnimated) }

    LaunchedEffect(Unit) {
        animatableColorHighlight.animateTo(
            targetValue = underlineColor,
            animationSpec = tween(
                durationMillis = 2500,
                easing = LinearEasing
            )
        )
    }

    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle(
            fontSize = 16.sp,
            fontFamily = regular,
            color = firstColorBeforeAnimated
        )) {
            append(textString)
        }
            val startIndexForStyle = textString.indexOf(targetWord.value)
            if (startIndexForStyle != -1) {
                val endIndexForStyle = startIndexForStyle + targetWord.value.length
                addStyle(
                    style = SpanStyle(
                        fontSize = 16.sp,
                        fontFamily = regular,
                        color = animatableColorHighlight.value
                    ),
                    start = startIndexForStyle,
                    end = endIndexForStyle
                )
                addStringAnnotation(
                    tag = "here",
                    annotation = targetWord.value,
                    start = startIndexForStyle,
                    end = endIndexForStyle
                )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = MaterialTheme.colorScheme.primary),
                onClick = {}
            )
    ) {
        ClickableText(
            text = annotatedText,
            style = TextStyle(fontSize = 16.sp, fontFamily = regular),
            onClick = { offset ->
                annotatedText.getStringAnnotations(
                    tag = "here",
                    start = offset,
                    end = offset
                ).firstOrNull()?.let { annotation ->
                    onClickGoToFavorites.invoke()
                }
            },
            onTextLayout = { layoutResult ->
                val textBounds = layoutResult.getBoundingBoxesForRange(
                    start = startIndex.intValue,
                    end = endIndex.intValue
                )
                onDraw = {
                    for (bound in textBounds) {
                        val underline = bound.copy(top = bound.bottom)
                        textWidth.floatValue = underline.bottomRight.x
                        drawLine(
                            color = animatableColorHighlight.value,
                            start = underline.topLeft,
                            end = Offset(textWidth.floatValue, underline.top),
                            strokeWidth = 4f
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind { onDraw() }
        )
    }
}


fun TextLayoutResult.getBoundingBoxesForRange(start: Int, end: Int): List<Rect> {
    var prevRect: Rect? = null
    var firstLineCharRect: Rect? = null
    val boundingBoxes = mutableListOf<Rect>()
    for (i in start..end) {
        val rect = getBoundingBox(i)
        val isLastRect = i == end

        if (isLastRect && firstLineCharRect == null) {
            firstLineCharRect = rect
            prevRect = rect
        }

        if (!isLastRect && rect.right == 0f) continue

        if (firstLineCharRect == null) {
            firstLineCharRect = rect
        } else if (prevRect != null) {
            if (prevRect.bottom != rect.bottom || isLastRect) {
                boundingBoxes.add(
                    firstLineCharRect.copy(right = prevRect.right)
                )
                firstLineCharRect = rect
            }
        }
        prevRect = rect
    }
    return boundingBoxes
}
@Composable
fun QuickFavoriteList(favoriteImages: List<FavoriteImages>,) {
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(top = 8.dp)
    ) {
        items(favoriteImages.size) { index ->
            val favoriteImage = favoriteImages[index]
            var visible by remember { mutableStateOf(false) }
            val animatedOffset by animateDpAsState(
                targetValue = if (visible) 0.dp else 300.dp,
                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing), label = ""
            )

            LaunchedEffect(key1 = index) {
                delay(300 * index.toLong())
                visible = true
            }

            FavoriteListItem(
                modifier = Modifier
                    .offset(x = animatedOffset),
                favoriteImage = favoriteImage
            )
        }
    }
}
@Composable
fun FavoriteListItem(
    modifier: Modifier = Modifier,
    favoriteImage: FavoriteImages,
) {
    SubcomposeAsyncImage(
        modifier = modifier
            .size(48.dp)
            .clip(RoundedCornerShape(8.dp)),
        model = favoriteImage.url,
        contentDescription = favoriteImage.name,
        loading = {
            ImageLoadingState()
        },
        contentScale = ContentScale.FillBounds
    )
}

