package com.oguzdogdu.walliescompose.features.profiledetail.components

import android.graphics.Typeface
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import coil.compose.AsyncImage
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.domain.model.userdetail.UserDetails
import com.oguzdogdu.walliescompose.features.profiledetail.state.ProfileDetailState

@Composable
fun CircleImageWithBorderAndText(
    modifier: Modifier = Modifier,
    profileDetailState: ProfileDetailState?,
    text: String
) {
    val colorOfCircle: Color = MaterialTheme.colorScheme.secondaryContainer
    val context = LocalContext.current
    val typefaceOfText = ResourcesCompat.getFont(context, R.font.googlesansmedium) ?: Typeface.DEFAULT

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val animatedStrokeWidth = infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val textMeasurer = rememberTextMeasurer()
    val textStyle = TextStyle(
        color = Color.White,
        fontSize = 10.sp,
        fontFamily = FontFamily(typefaceOfText),
        textAlign = TextAlign.Center
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(160.dp)
            .clip(CircleShape)
    ) {
        AsyncImage(
            model = profileDetailState?.userDetails?.profileImage,
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
        )
        if (profileDetailState?.userDetails?.forHire == true) {
            val rectWidth by remember { mutableFloatStateOf(220f) }
            val rectHeight by remember { mutableFloatStateOf(72f) }
            val cornerRadius by remember { mutableFloatStateOf(16f) }
            val textLayoutResult = textMeasurer.measure(
                text = AnnotatedString(text),
                style = textStyle,
                constraints = Constraints(
                    minWidth = 0,
                    maxWidth = rectWidth.toInt()
                )
            )

            Canvas(modifier = Modifier.matchParentSize()) {
                val rectLeft = (size.width - rectWidth) / 2
                val rectTop = size.height - rectHeight

                drawArc(
                    brush = SolidColor(colorOfCircle.copy(alpha = animatedStrokeWidth.value)),
                    startAngle = 130f,
                    sweepAngle = 280f,
                    useCenter = false,
                    style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                )

                drawRoundRect(
                    color = colorOfCircle.copy(alpha = animatedStrokeWidth.value),
                    topLeft = Offset(rectLeft, rectTop),
                    size = Size(rectWidth, rectHeight),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                )

                val textOffset = Offset(
                    x = rectLeft + (rectWidth - textLayoutResult.size.width) / 2,
                    y = rectTop + (rectHeight - textLayoutResult.size.height) / 3
                )

                drawText(
                    textLayoutResult = textLayoutResult,
                    color = Color.White,
                    topLeft = textOffset
                )
            }
        }
    }
}
@Preview
@Composable
fun CircleImageWithBorderAndTextPreview() {
    CircleImageWithBorderAndText(
        profileDetailState = ProfileDetailState(userDetails =
        UserDetails(
            name = "James",
            bio = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
            profileImage = null,
            postCount = 100,
            followingCount = 100,
            followersCount = 100,
            portfolioUrl = null,
            location = "England",
            username = null,
            totalPhotos = null,
            totalCollections = null,
            verification = "",
            instagram = null,
            twitter = null,
            portfolioList = emptyList(),
            portfolio = null,
            forHire = false
        )
        ), text = "For Hire"
    )
}
