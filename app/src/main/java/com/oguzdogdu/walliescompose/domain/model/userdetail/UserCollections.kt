package com.oguzdogdu.walliescompose.domain.model.userdetail

import kotlinx.serialization.Serializable

@Serializable
data class UserCollections(
    val id: String?,
    val title: String?,
    val photo: String?,
    val desc: String?,
    val likes: Int?
)
