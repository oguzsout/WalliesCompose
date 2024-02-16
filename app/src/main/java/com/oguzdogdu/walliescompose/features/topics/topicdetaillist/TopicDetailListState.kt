package com.oguzdogdu.walliescompose.features.topics.topicdetaillist

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import com.oguzdogdu.walliescompose.domain.model.topics.TopicDetail

@Immutable
data class TopicListState(val topicList: PagingData<TopicDetail> = PagingData.empty())

