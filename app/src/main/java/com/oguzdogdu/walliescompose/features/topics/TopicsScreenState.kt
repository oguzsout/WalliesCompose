package com.oguzdogdu.walliescompose.features.topics

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import com.oguzdogdu.walliescompose.domain.model.topics.Topics

@Immutable
data class TopicsScreenState(
    val topics: PagingData<Topics> = PagingData.empty()
)
