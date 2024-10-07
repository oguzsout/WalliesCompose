package com.oguzdogdu.walliescompose.features.home.components

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.StartOffsetType
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.ui.theme.regular

@Composable
fun PhotoByOrientationCard(modifier: Modifier = Modifier) {
    val containerColor = Brush.linearGradient(listOf(Color.Blue.copy(alpha = 0.5f), Color.Red.copy(alpha = 0.5f)))
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val textRotation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutCubic, delayMillis = 2000),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(offsetMillis = 1, offsetType = StartOffsetType.Delay)
        ), label = ""
    )
    Card(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(8.dp)
            .background(containerColor, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors().copy(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(space = 16.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_rotate_image),
                contentDescription = "",
                tint = Color.Unspecified,
                modifier = Modifier.graphicsLayer {
                    rotationZ = textRotation.value
                }
            )
           ExploreText()
        }
    }
}
@Composable
fun ExploreText(modifier: Modifier = Modifier) {
    val portrait = stringResource(id = R.string.portrait)
    val landscape = stringResource(id = R.string.landscape)
    val exploreText = stringResource(id = R.string.explore_images, portrait, landscape)
    val underlineColor = MaterialTheme.colorScheme.secondaryContainer
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            val portraitStart = exploreText.indexOf(portrait)
            val landscapeStart = exploreText.indexOf(landscape)

            append(exploreText)

            addStyle(
                style = SpanStyle(color = underlineColor,fontWeight = FontWeight.Bold),
                start = portraitStart,
                end = portraitStart + portrait.length
            )

            addStyle(
                style = SpanStyle(color = underlineColor,fontWeight = FontWeight.Bold),
                start = landscapeStart,
                end = landscapeStart + landscape.length
            )
        },
        color = Color.White,
        fontSize = 16.sp,
        fontFamily = regular,
        textAlign = TextAlign.Start
    )
}
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PhotoByOrientationCardPreview(modifier: Modifier = Modifier) {
    PhotoByOrientationCard()
}