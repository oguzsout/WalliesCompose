package com.oguzdogdu.walliescompose.features.popular

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import com.oguzdogdu.walliescompose.domain.model.popular.PopularImage

@Immutable
data class PopularScreenState(
    val popular: PagingData<PopularImage> = PagingData.empty()
)

