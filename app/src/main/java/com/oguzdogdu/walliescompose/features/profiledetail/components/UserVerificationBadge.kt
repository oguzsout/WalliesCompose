package com.oguzdogdu.walliescompose.features.profiledetail.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI

@Composable
fun UserVerificationBadge(
    visible: Boolean,
    petalColor: Color,
    centerColor: Color,
    checkmarkColor: Color,
    petalCount: Int = 8
) {
    val checkmarkProgress = remember { Animatable(0f) }
    val canvasProgress = remember { Animatable(0f) }
    val textScale = remember { Animatable(0f) }
    val visibility = rememberUpdatedState(visible)
    var showVerifyIcon by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(visibility.value) {
        delay(2000)
        showVerifyIcon = true
        if (visibility.value) {
            launch {
                textScale.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 1500,
                        easing = EaseOutCubic
                    )
                )
            }
            launch {
                canvasProgress.animateTo(
                    targetValue = 360f,
                    animationSpec = tween(
                        durationMillis = 1500,
                        easing = LinearEasing
                    )
                )
            }
            launch {
                checkmarkProgress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 2000,
                        easing = EaseOutCubic
                    )
                )
            }
        } else {
            textScale.snapTo(0f)
            canvasProgress.snapTo(0f)
            checkmarkProgress.snapTo(0f)
        }
    }
    if (showVerifyIcon) {
            Canvas(modifier = Modifier.size(24.dp)) {
                val radius = size.width.coerceAtMost(size.height)

                val petalLength = radius * 0.9f
                val petalWidth = (2 * PI / petalCount).toFloat() * radius * 0.7f

                repeat(petalCount) { i ->
                    rotate(canvasProgress.value / petalCount * i, center) {
                        drawOval(
                            color = petalColor,
                            topLeft = Offset(center.x - petalWidth / 2, center.y - radius * 0.4f),
                            size = Size(petalWidth, petalLength),
                        )
                    }
                }

                drawCircle(
                    color = centerColor,
                    radius = radius * 0.3f,
                    center = center,
                    style = Stroke(width = 1.dp.toPx())
                )
                val checkmarkPath = Path().apply {
                    val checkSize = radius * 0.3f
                    moveTo(center.x - checkSize * 0.5f, center.y)
                    lineTo(center.x - checkSize * 0.1f, center.y + checkSize * 0.4f)
                    lineTo(center.x + checkSize * 0.5f, center.y - checkSize * 0.4f)
                }
                val pathMeasure = PathMeasure()
                pathMeasure.setPath(checkmarkPath, false)

                val animatedPath = Path()
                pathMeasure.getSegment(
                    startDistance = 0f,
                    stopDistance = pathMeasure.length * checkmarkProgress.value,
                    destination = animatedPath
                )
                drawPath(
                    path = animatedPath,
                    color = checkmarkColor,
                    style = Stroke(
                        width = 1.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
        }
    }