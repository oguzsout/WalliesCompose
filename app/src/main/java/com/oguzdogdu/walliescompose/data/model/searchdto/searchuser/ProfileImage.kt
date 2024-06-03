package com.oguzdogdu.walliescompose.data.model.searchdto.searchuser

import kotlinx.serialization.Serializable

@Serializable
data class ProfileImage(
    val large: String,
    val medium: String,
    val small: String
)