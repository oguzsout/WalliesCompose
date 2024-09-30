package com.oguzdogdu.walliescompose.features.home.state

import androidx.compose.runtime.Stable
import com.oguzdogdu.walliescompose.domain.model.latest.LatestImage
import com.oguzdogdu.walliescompose.domain.model.popular.PopularImage
import com.oguzdogdu.walliescompose.domain.model.random.RandomImage
import com.oguzdogdu.walliescompose.domain.model.topics.Topics

@Stable
sealed class HomeUIState {
    data class Loading(val loading: Boolean = false) : HomeUIState()
    data class Error(val errorMessage: String = "") : HomeUIState()
    data class Success(
        val random: List<RandomImage> = emptyList(),
        val topics: List<Topics> = emptyList(),
        val popular: List<PopularImage> = emptyList(),
        val latest: List<LatestImage> = emptyList()
    ) : HomeUIState() {
        fun isEmpty(): Boolean = topics.isEmpty() and popular.isEmpty() and latest.isEmpty()
    }
}
