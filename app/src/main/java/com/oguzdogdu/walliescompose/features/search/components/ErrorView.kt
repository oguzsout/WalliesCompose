package com.oguzdogdu.walliescompose.features.search.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ErrorItem(modifier: Modifier = Modifier, message: String?) {
    Card(
        modifier = modifier
            .padding(6.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.Red)
                .padding(8.dp)
        ) {
            Image(
                modifier = modifier
                    .clip(CircleShape)
                    .width(24.dp)
                    .height(24.dp),
                imageVector = Icons.Default.Warning,
                contentDescription = "",
                colorFilter = ColorFilter.tint(Color.White)
            )
            Text(
                color = Color.White,
                text = message.orEmpty(),
                fontSize = 16.sp,
                modifier = modifier
                    .padding(start = 12.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}