package com.oguzdogdu.walliescompose.data.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

private val defaultModifier = Modifier
    .fillMaxSize()
    .padding(all = 24.dp)

@Composable
fun LoadingState(modifier: Modifier = Modifier) {
    Box(modifier = modifier.wrapContentSize()) {
        CircularProgressIndicator(
            modifier = modifier.heightIn(min = 16.dp, max = 32.dp),
            color = MaterialTheme.colorScheme.onBackground,
            strokeCap = StrokeCap.Round
        )
    }

}