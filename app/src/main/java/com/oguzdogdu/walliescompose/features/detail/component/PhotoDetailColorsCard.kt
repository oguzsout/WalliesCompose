package com.oguzdogdu.walliescompose.features.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.features.detail.DetailState
import com.oguzdogdu.walliescompose.ui.theme.regular

@Composable
fun PhotoDetailColorsCard(photoDetailState: DetailState) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ColorsCardItems(photoDetailState = photoDetailState )
    }
}

@Composable
fun ColorsCardItems(photoDetailState: DetailState) {
    val context = LocalContext.current
    val readyForShownList = listOf(
        DetailPhotoColors(
            color = photoDetailState.detail?.color.takeIf { isValidColor(it.toString()) } ?: "#FFFFFF",
            description = DetailPhotoColorsTexts(
                title = context.getString(R.string.text_detail_color),
                description = photoDetailState.detail?.color.orEmpty()
            )
        )
    )

    LazyColumn(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        items(items = readyForShownList) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(color = Color(android.graphics.Color.parseColor(it.color)), shape = RoundedCornerShape(24.dp))
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "${it.description.title}: ${it.description.description}",
                    fontSize = 14.sp,
                    fontFamily = regular,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Stable
data class DetailPhotoColors(val color: String, val description: DetailPhotoColorsTexts)

@Stable
data class DetailPhotoColorsTexts( val title: String, val description: String)
fun isValidColor(color: String): Boolean {
    return try {
        android.graphics.Color.parseColor(color)
        true
    } catch (e: IllegalArgumentException) {
        false
    }
}