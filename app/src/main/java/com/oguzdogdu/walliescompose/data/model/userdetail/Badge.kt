package com.oguzdogdu.walliescompose.data.model.userdetail

import kotlinx.serialization.Serializable

@Serializable
data class Badge (
    val title: String,
    val primary: Boolean,
    val slug: String,
    val link: String?
)