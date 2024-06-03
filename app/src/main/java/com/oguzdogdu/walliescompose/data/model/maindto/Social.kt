package com.oguzdogdu.walliescompose.data.model.maindto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Social(
    @SerializedName("instagram_username")
    val instagramUsername: String?,
    @SerializedName("portfolio_url")
    val portfolioUrl: String?,
    @SerializedName("twitter_username")
    val twitterUsername: String?
)