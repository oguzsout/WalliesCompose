package com.oguzdogdu.walliescompose.util

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

inline fun Modifier.noRippleClickable(
    crossinline onClick: () -> Unit
): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

fun Modifier.moveScaffoldOffset(move: Boolean): Modifier = composed {
    val progressOffsetScale = remember { Animatable(0f) }
    LaunchedEffect(move) {
        when(move) {
            true -> {
                progressOffsetScale.animateTo(
                    targetValue = 48f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessVeryLow
                    )
                )
            }
            false -> {
                progressOffsetScale.animateTo(
                    targetValue = 0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessVeryLow
                    )
                )
            }
        }
    }
    this.offset {
        IntOffset(x = 0, y = progressOffsetScale.value.toInt())
    }
}

fun Modifier.moveScaffoldPadding(move: Boolean): Modifier = composed {
    val progressPaddingValue = remember { Animatable(0f) }
    LaunchedEffect(move) {
        when(move) {
            true -> {
                progressPaddingValue.animateTo(
                    targetValue = 8f,
                    animationSpec = tween(400, easing = EaseInOutCubic)
                )
            }
            false -> {
                progressPaddingValue.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(400, easing = EaseInOutCubic)
                )
            }
        }
    }
    this.padding(progressPaddingValue.value.dp)
}

fun Modifier.moveScaffoldClip(move: Boolean): Modifier = composed {
    val progressClipValue = remember { Animatable(0f) }
    LaunchedEffect(move) {
        when(move) {
            true -> {
                progressClipValue.animateTo(
                    targetValue = 16f,
                    animationSpec = tween(100, easing = LinearEasing)
                )
            }
            false -> {
                progressClipValue.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(400, easing = LinearEasing)
                )
            }
        }
    }
    this.clip(RoundedCornerShape(progressClipValue.value.dp) )
}