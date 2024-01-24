package com.oguzdogdu.walliescompose.features.topics

import androidx.paging.PagingData
import com.oguzdogdu.walliescompose.domain.model.topics.Topics

data class TopicsScreenState(
    val topics: PagingData<Topics> = PagingData.empty()
)
