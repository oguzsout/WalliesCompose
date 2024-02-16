package com.oguzdogdu.walliescompose.features.detail

import androidx.compose.runtime.Immutable
import com.oguzdogdu.walliescompose.domain.model.detail.Photo
import com.oguzdogdu.walliescompose.domain.model.favorites.FavoriteImages

@Immutable
data class DetailState(
    val loading: Boolean = false,
    val detail: Photo? = null,
    val errorMessage: String? = null,
    val favorites:FavoriteImages? = null
)

