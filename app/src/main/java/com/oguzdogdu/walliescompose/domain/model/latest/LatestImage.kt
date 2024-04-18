package com.oguzdogdu.walliescompose.domain.model.latest

import kotlinx.serialization.Serializable

@Serializable
data class LatestImage(
    val id: String?,
    val url: String?,
    val imageDesc: String?
)
