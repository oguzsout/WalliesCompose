package com.oguzdogdu.walliescompose.data.model.maindto

import com.google.gson.annotations.SerializedName

data class Sponsorship(
    @SerializedName("sponsor")
    val sponsor: Sponsor?,
    @SerializedName("tagline")
    val tagline: String?,
    @SerializedName("tagline_url")
    val taglineUrl: String?
)