package com.oguzdogdu.walliescompose.data.model.topics


import com.google.gson.annotations.SerializedName

data class Social(
    @SerializedName("instagram_username") val instagramUsername: String?,
    @SerializedName("paypal_email") val paypalEmail: String?,
    @SerializedName("portfolio_url") val portfolioUrl: String?,
    @SerializedName("twitter_username") val twitterUsername: String?
)