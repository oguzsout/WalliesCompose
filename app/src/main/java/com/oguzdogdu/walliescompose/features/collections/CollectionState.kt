package com.oguzdogdu.walliescompose.features.collections

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.oguzdogdu.walliescompose.domain.model.collections.WallpaperCollections

@Immutable
data class CollectionState(
    val collectionsListType: ListType = ListType.GRID
)
@Stable
enum class ListType{
    VERTICAL,
    GRID
}


