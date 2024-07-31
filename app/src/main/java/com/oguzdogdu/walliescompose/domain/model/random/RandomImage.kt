package com.oguzdogdu.walliescompose.domain.model.random

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class RandomImage(
    val id: String?,
    val url: String?,
    val imageDesc: String?,
    val color: String?,
    val username: String?,
    val userImage: String?
)
