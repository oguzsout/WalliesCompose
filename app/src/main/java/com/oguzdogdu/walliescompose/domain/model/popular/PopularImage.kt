package com.oguzdogdu.walliescompose.domain.model.popular

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class PopularImage(
    val id : String?,
    val url:String?,
    val imageDesc: String?
)
