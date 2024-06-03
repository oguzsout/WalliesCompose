package com.oguzdogdu.walliescompose.data.model.userdetail

import kotlinx.serialization.Serializable

@Serializable
data class Social(
    val instagram_username: String?,
    val portfolio_url: String?,
    val twitter_username: String?
)