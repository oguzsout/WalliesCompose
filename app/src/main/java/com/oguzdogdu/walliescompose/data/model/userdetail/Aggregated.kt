package com.oguzdogdu.walliescompose.data.model.userdetail

import kotlinx.serialization.Serializable

@Serializable
data class Aggregated(
    val source: Source?,
    val title: String?,
    val type: String?
)