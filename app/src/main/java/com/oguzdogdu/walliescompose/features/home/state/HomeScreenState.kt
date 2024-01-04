package com.oguzdogdu.walliescompose.features.home.state

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.oguzdogdu.walliescompose.domain.model.latest.LatestImage
import com.oguzdogdu.walliescompose.domain.model.popular.PopularImage
import com.oguzdogdu.walliescompose.domain.model.topics.Topics

@Immutable
data class HomeScreenState(
    val loading: Boolean = false,
    val error: String? = null,
    @Stable
    val topics: List<Topics> = emptyList(),
    @Stable
    val popular: List<PopularImage> = emptyList(),
    @Stable
    val latest: List<LatestImage> = emptyList()
)
