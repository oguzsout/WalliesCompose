package com.oguzdogdu.walliescompose.domain.model.collections

import androidx.compose.runtime.Immutable
import com.oguzdogdu.walliescompose.data.model.topics.ProfileImage
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class WallpaperCollections(
    val id: String?,
    val title: String?,
    val photo: String?,
    val desc: String?,
    val likes: Int?,
    val updatedAt: String?,
    val name: String?,
    val profileImage: String?,
    val totalPhotos: Int?
)
