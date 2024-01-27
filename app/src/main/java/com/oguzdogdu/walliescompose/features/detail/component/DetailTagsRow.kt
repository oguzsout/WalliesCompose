package com.oguzdogdu.walliescompose.features.detail.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzdogdu.walliescompose.domain.model.detail.Photo
import com.oguzdogdu.walliescompose.ui.theme.regular

@Composable
fun DetailTagsRow(modifier: Modifier, detail: Photo?,onTagClick:(String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(8.dp),
    ) {
        items(detail?.tag.orEmpty(), key = { item ->
            item.hashCode()
        }) { item ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
                shape = CircleShape.copy(CornerSize(16.dp)),
                modifier = modifier
                    .wrapContentSize()
                    .padding(4.dp)
                    .clickable {
                        onTagClick.invoke(item.orEmpty())
                    },
            ) {
                Text(
                    text = item.orEmpty(),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = modifier.padding(8.dp),
                    fontFamily = regular,
                    fontSize = 12.sp
                )
            }
        }
    }
}