package com.oguzdogdu.walliescompose.domain.model.search

import kotlinx.serialization.Serializable

@Serializable
data class SearchPhoto(
    val id: String?,
    val url: String?,
    val imageDesc: String?
)
