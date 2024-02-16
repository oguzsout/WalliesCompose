package com.oguzdogdu.walliescompose.features.home.state

import androidx.compose.runtime.Immutable
import com.oguzdogdu.walliescompose.domain.model.latest.LatestImage
import com.oguzdogdu.walliescompose.domain.model.popular.PopularImage
import com.oguzdogdu.walliescompose.domain.model.topics.Topics

@Immutable
data class HomeScreenState(
    val loading: Boolean = false,
    val error: String? = null,
    val topics: List<Topics> = emptyList(),
    val popular: List<PopularImage> = emptyList(),
    val latest: List<LatestImage> = emptyList()
)
