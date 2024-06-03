package com.oguzdogdu.walliescompose.data.model.topics


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Urls(
    @SerializedName("full") val full: String?,
    @SerializedName("raw") val raw: String?,
    @SerializedName("regular") val regular: String?,
    @SerializedName("small") val small: String?,
    @SerializedName("small_s3") val smallS3: String?,
    @SerializedName("thumb") val thumb: String?
)