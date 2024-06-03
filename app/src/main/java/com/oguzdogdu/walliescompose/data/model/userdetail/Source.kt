package com.oguzdogdu.walliescompose.data.model.userdetail

import kotlinx.serialization.Serializable

@Serializable
data class Source(
    val ancestry: Ancestry?,
    val cover_photo: CoverPhoto?,
    val description: String?,
    val meta_description: String?,
    val meta_title: String?,
    val subtitle: String?,
    val title: String?
)