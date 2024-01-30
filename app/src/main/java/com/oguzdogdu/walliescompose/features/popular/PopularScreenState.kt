package com.oguzdogdu.walliescompose.features.popular

import androidx.paging.PagingData
import com.oguzdogdu.walliescompose.domain.model.popular.PopularImage

data class PopularScreenState(
    val popular: PagingData<PopularImage> = PagingData.empty()
)

