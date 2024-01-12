package com.oguzdogdu.walliescompose.features.favorites.state

import com.oguzdogdu.walliescompose.domain.model.favorites.FavoriteImages

data class FavoriteScreenState(
    val loading: Boolean = false,
    val favorites: List<FavoriteImages>? = emptyList(),
    val error: String? = ""
)
