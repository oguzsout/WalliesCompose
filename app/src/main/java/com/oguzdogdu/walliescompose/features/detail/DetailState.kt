package com.oguzdogdu.walliescompose.features.detail

import com.oguzdogdu.walliescompose.domain.model.detail.Photo

data class DetailState(
    val loading: Boolean = false,
    val detail: Photo? = null,
    val errorMessage: String? = null
)

