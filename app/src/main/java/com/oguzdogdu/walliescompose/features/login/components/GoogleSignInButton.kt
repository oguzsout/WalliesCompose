package com.oguzdogdu.walliescompose.features.login.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.ui.theme.LightColorPalette
import com.oguzdogdu.walliescompose.ui.theme.medium

@Composable
fun ButtonGoogleSignIn(
    onGoogleSignInButtonClick: () -> Unit, loading: Boolean = false, modifier: Modifier = Modifier
) {
    val isLoading by rememberUpdatedState(newValue = loading)
    val transition = updateTransition(targetState = isLoading, label = "button state")
    val primaryColorForArc by remember { mutableStateOf(LightColorPalette.primary) }
    val buttonWidthFactor by transition.animateFloat(
        transitionSpec = {
            tween(
                durationMillis = 1000, easing = LinearEasing
            )
        }, label = "button width"
    ) { state ->
        if (state) 0f else 1f
    }

    val borderColor by transition.animateColor(
        transitionSpec = {
            tween(
                durationMillis = 700
            )
        }, label = "border color"
    ) { state ->
        if (state) MaterialTheme.colorScheme.secondaryContainer else Color.Gray
    }

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val animatedRotation by if (isLoading) {
        infiniteTransition.animateFloat(
            initialValue = 0f, targetValue = 360f, animationSpec = infiniteRepeatable(
                animation = tween(750, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Restart
            ), label = "rotation"
        )
    } else {
        remember { mutableFloatStateOf(0f) }
    }

    val colorOfProgressArc by if (isLoading) {
        infiniteTransition.animateColor(
            initialValue = MaterialTheme.colorScheme.secondaryContainer,
            targetValue = primaryColorForArc,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 500, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "progress color"
        )
    } else {
        remember { mutableStateOf(primaryColorForArc) }
    }

    Button(
        onClick = {
            onGoogleSignInButtonClick.invoke()
        },
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(buttonWidthFactor)
            .height(40.dp)
            .then(
                if (buttonWidthFactor > 0.1f) Modifier.border(
                    width = 2.dp, color = borderColor, shape = RoundedCornerShape(24.dp)
                ) else Modifier
            )
            .animatedButtonProgressBar(
                color = colorOfProgressArc,
                rotation = animatedRotation,
                stateOfLoading = isLoading,
                stateOfButtonWidth = buttonWidthFactor
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        AnimatedVisibility(
            visible = buttonWidthFactor > 0.5f, enter = scaleIn(
                tween(750)
            ), exit = scaleOut(
                animationSpec = tween(750)
            )
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google), contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(id = R.string.sign_in_with_google),
                    fontSize = 14.sp,
                    fontFamily = medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Stable
fun Modifier.animatedButtonProgressBar(
    color: Color, rotation: Float, stateOfLoading: Boolean, stateOfButtonWidth: Float
) = if (stateOfLoading && stateOfButtonWidth <= 0.1f) this.drawBehind {
    drawIntoCanvas { canvas ->
        val arcSize = 44.dp.toPx()
        val arcOffsetX = (size.width - arcSize) / 2
        val arcOffsetY = (size.height - arcSize) / 2
        val arcRect = Rect(arcOffsetX, arcOffsetY, arcOffsetX + arcSize, arcOffsetY + arcSize)

        val paint = Paint().apply {
            this.color = color
            style = PaintingStyle.Stroke
            strokeWidth = 3.dp.toPx()
            strokeCap = StrokeCap.Round
        }

        withTransform({
            rotate(rotation, center)
        }) {
            canvas.drawArc(
                rect = arcRect,
                startAngle = 90f,
                sweepAngle = 280f,
                useCenter = false,
                paint = paint
            )
        }
    }
} else this