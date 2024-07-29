package com.oguzdogdu.walliescompose.util

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
@Stable
sealed class ListItem {
    @Stable
    data class Header(@StringRes val titleRes: Int? = null,) : ListItem()
    @Stable
    data class Content(
        val id: Int, @StringRes val description: Int? = null,
        @DrawableRes val icon: Int? = null, val arrow: Boolean = false
    ) : ListItem()
}

@Stable
sealed interface BaseLazyColumnEvents

@Stable
data class ListProperties(
    val items: List<ListItem> = emptyList(),
    val cells: Int = 1,
    val isLoading: Boolean = false,
    val bottomLoadingForPagination: Boolean = false
)

@Composable
fun BaseLazyColumn(
    listProperties: ListProperties,
    bottomLoadingContent: @Composable (() -> Unit),
    reverseLayout: Boolean = false,
    onScrollEndReached: ((Boolean) -> Unit)? = null,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    itemContent: @Composable (ListItem) -> Unit,
    emptyContent: @Composable () -> Unit = { Text("No items available") },
    loadingContent: @Composable () -> Unit = { CircularProgressIndicator() },
    modifier: Modifier = Modifier
) {

    val lazyListState = rememberLazyGridState()
    val listPropertiesState by rememberUpdatedState(
        newValue = ListProperties(
            items = listProperties.items,
            cells = listProperties.cells,
            isLoading = listProperties.isLoading,
            bottomLoadingForPagination = listProperties.bottomLoadingForPagination
        )
    )
    val emptyList by remember {
        derivedStateOf {
            listProperties.items.isEmpty()
        }
    }
    var listEndBottomLoading by remember { mutableStateOf(false) }
    val bottomLockForPagination =
        rememberUpdatedState(newValue = listPropertiesState.bottomLoadingForPagination)
    val listItemCache = remember { mutableIntStateOf(listPropertiesState.items.size) }

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleItemIndex ->
                if (lastVisibleItemIndex == listProperties.items.size - 1) {
                    onScrollEndReached?.invoke(listProperties.items.size == listItemCache.intValue)
                }
            }
    }

    LaunchedEffect(key1 = Unit) {
        delay(2000)
        listItemCache.intValue = listPropertiesState.items.size
    }

    LaunchedEffect(listPropertiesState.items.size) {
        if (listPropertiesState.items.size != listItemCache.intValue) {
            listEndBottomLoading = false
        } else listEndBottomLoading = true
    }

    when {
        listProperties.isLoading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                loadingContent()
            }
        }
        emptyList -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                emptyContent()
            }
        }
        else -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(listPropertiesState.cells),
                state = lazyListState,
                modifier = modifier,
                reverseLayout = reverseLayout,
                verticalArrangement = verticalArrangement,
                horizontalArrangement = horizontalArrangement
            ) {
                items(items = listPropertiesState.items, key = {
                    when (it) {
                        is ListItem.Header -> it.titleRes.hashCode()
                        is ListItem.Content -> it.id.hashCode()
                    }
                }) { item ->
                    itemContent(item)
                }
                if (bottomLockForPagination.value && listEndBottomLoading) {
                    item(span = { GridItemSpan(currentLineSpan = listProperties.cells) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            bottomLoadingContent()
                        }
                    }
                }
            }
        }
    }
}