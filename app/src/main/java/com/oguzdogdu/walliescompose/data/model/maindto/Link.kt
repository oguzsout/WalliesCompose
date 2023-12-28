package com.oguzdogdu.walliescompose.data.model.maindto

import com.google.gson.annotations.SerializedName


data class Link(
    @SerializedName("download")
    val download: String,
    @SerializedName("download_location")
    val downloadLocation: String,
    @SerializedName("html")
    val html: String,
    @SerializedName("self")
    val self: String
)