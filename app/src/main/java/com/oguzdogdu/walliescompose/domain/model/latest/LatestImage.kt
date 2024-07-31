package com.oguzdogdu.walliescompose.domain.model.latest

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class LatestImage(
    val id: String?,
    val url: String?,
    val imageDesc: String?
)
