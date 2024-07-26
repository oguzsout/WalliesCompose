package com.oguzdogdu.walliescompose.features.collections

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
data class CollectionState(
    val collectionsListType: ListType = ListType.GRID,
    val sheetState: Boolean = false,
    val choisedFilter: Int = 0
)
@Stable
enum class ListType{
    VERTICAL,
    GRID
}


