package com.oguzdogdu.walliescompose.data.model.userdetail

import kotlinx.serialization.Serializable

@Serializable
data class Nature(
    val approved_on: String?,
    val status: String?
)