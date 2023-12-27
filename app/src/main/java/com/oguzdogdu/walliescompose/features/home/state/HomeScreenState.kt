package com.oguzdogdu.walliescompose.features.home.state

import com.oguzdogdu.walliescompose.domain.model.Topics

sealed interface HomeScreenState {
    data class TopicsTitleList(
        val loading: Boolean = false,
        val error: String? = null,
        val topics: List<Topics> = emptyList()
    ) : HomeScreenState
}