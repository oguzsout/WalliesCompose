package com.oguzdogdu.walliescompose.domain.model.collections

import androidx.compose.runtime.Immutable

@Immutable
data class WallpaperCollections(
    val id: String?,
    val title: String?,
    val photo: String?,
    val desc: String?,
    val likes: Int?,
    val updatedAt: String?,
)
