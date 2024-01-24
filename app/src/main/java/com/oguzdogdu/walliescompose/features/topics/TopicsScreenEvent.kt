package com.oguzdogdu.walliescompose.features.topics

sealed class TopicsScreenEvent {
    data object FetchTopicsData : TopicsScreenEvent()
}
