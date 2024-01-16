package com.oguzdogdu.walliescompose.features.detail

import com.oguzdogdu.walliescompose.domain.model.detail.Photo
import com.oguzdogdu.walliescompose.domain.model.favorites.FavoriteImages

data class DetailState(
    val loading: Boolean = false,
    val detail: Photo? = null,
    val errorMessage: String? = null,
    val favorites:FavoriteImages? = null
)

