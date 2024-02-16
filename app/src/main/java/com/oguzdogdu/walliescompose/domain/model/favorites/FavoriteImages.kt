package com.oguzdogdu.walliescompose.domain.model.favorites

import androidx.compose.runtime.Immutable

@Immutable
data class FavoriteImages(
    val id: String? = null,
    val url: String? = null,
    val profileImage: String? = null,
    val name: String? = null,
    val portfolioUrl: String? = null,
    var isChecked: Boolean = false
)
