package com.oguzdogdu.walliescompose.data.model.userdetail

import kotlinx.serialization.Serializable

@Serializable
data class Subcategory(
    val pretty_slug: String?,
    val slug: String?
)