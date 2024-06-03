package com.oguzdogdu.walliescompose.domain.model.userdetail

import kotlinx.serialization.Serializable

@Serializable
data class UsersPhotos(
    val id: String?,
    val url: String?,
    val imageDesc: String?
)
