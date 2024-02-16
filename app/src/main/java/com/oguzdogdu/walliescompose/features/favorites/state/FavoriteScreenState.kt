package com.oguzdogdu.walliescompose.features.favorites.state

import androidx.compose.runtime.Immutable
import com.oguzdogdu.walliescompose.domain.model.favorites.FavoriteImages

@Immutable
data class FavoriteScreenState(
    val loading: Boolean = false,
    val favorites: List<FavoriteImages>? = emptyList(),
    val error: String? = ""
)
