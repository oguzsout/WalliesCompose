package com.oguzdogdu.walliescompose.util

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun <T> LazyListScope.menuRow(
    data: List<T>,
    modifier: Modifier,
    itemContent: @Composable BoxScope.(T) -> Unit,
    background: @Composable (Int) -> CornerBasedShape,
    onClick: (Int) -> Unit
) {
    val size = data.count()
    val rows = if (size == 0) 0 else 1 + (size - 1)
    items(rows, key = { it.hashCode() }) { rowIndex ->
        Column(
            modifier = modifier.fillMaxWidth()
        ) {

            for (columnIndex in 0 until size) {
                val itemIndex = rowIndex * size + columnIndex
                if (itemIndex < size) {
                    val shape = background(itemIndex)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .clickable { onClick.invoke(itemIndex) }
                            .background(
                                color = MaterialTheme.colorScheme.tertiaryContainer,
                                shape = shape
                            )
                    ) {
                        itemContent(data[itemIndex])
                    }
                    if (rowIndex < rows - 1 && columnIndex < size - 1) {
                        Divider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 1.dp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}
