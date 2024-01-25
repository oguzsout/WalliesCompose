package com.oguzdogdu.walliescompose.features.topics.topicdetaillist

sealed class TopicDetailListEvent {
    data class FetchTopicListData(val idOrSlug: String?) : TopicDetailListEvent()
}
