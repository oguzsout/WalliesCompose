package com.oguzdogdu.walliescompose.features.home.state

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.oguzdogdu.walliescompose.domain.model.Topics

@Immutable
sealed interface HomeScreenState {
    @Immutable
    data class TopicsTitleList(
        val loading: Boolean = false,
        val error: String? = null,
        val topics: List<Topics> = emptyList()
    ) : HomeScreenState
}