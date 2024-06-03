package com.oguzdogdu.walliescompose.domain.model.collections

import kotlinx.serialization.Serializable

@Serializable
data class CollectionList(
    val id: String?,
    val url: String?,
    val desc: String?
)
