package com.oguzdogdu.walliescompose.domain.model.popular

import kotlinx.serialization.Serializable

@Serializable
data class PopularImage(
    val id : String?,
    val url:String?,
    val imageDesc: String?
)
