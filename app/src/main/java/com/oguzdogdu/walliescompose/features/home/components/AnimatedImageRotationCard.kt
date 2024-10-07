package com.oguzdogdu.walliescompose.features.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable

@Composable
fun ColumnScope.AnimatedImageRotationCard(visible: Boolean) {
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(tween(1250, easing = EaseOutBounce)) + scaleIn(tween(1000)),
        exit = shrinkVertically(
            tween(1250, easing = EaseOutBounce)
        ) + scaleOut(tween(1000))
    ) {
        PhotoByOrientationCard()
    }
}