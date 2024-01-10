package com.oguzdogdu.walliescompose.features.collections

import androidx.paging.PagingData
import com.oguzdogdu.walliescompose.domain.model.collections.WallpaperCollections

data class CollectionState(
    val collections: PagingData<WallpaperCollections> = PagingData.empty(),
    val sortedByTitlePagingData: PagingData<WallpaperCollections> = PagingData.empty(),
    val sortedByLikesPagingData: PagingData<WallpaperCollections> = PagingData.empty()
)


