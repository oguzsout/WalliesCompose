package com.oguzdogdu.walliescompose.features.home.state

import androidx.compose.runtime.Stable
import com.oguzdogdu.walliescompose.domain.model.popular.PopularImage
import com.oguzdogdu.walliescompose.domain.model.random.RandomImage
import com.oguzdogdu.walliescompose.domain.model.topics.Topics

    data class HomeUIState(
        val loading: Boolean = false,
        val errorMessage: String = "",
        val random: List<RandomImage> = emptyList(),
        val topics: List<Topics> = emptyList(),
        val popular: List<PopularImage> = emptyList(),
    ) {
        fun isEmpty(): Boolean = topics.isEmpty() and popular.isEmpty()
    }

