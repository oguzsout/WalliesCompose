package com.oguzdogdu.walliescompose.features.topics.topicdetaillist

import androidx.paging.PagingData
import com.oguzdogdu.walliescompose.domain.model.topics.TopicDetail


data class TopicListState(val topicList: PagingData<TopicDetail> = PagingData.empty())

