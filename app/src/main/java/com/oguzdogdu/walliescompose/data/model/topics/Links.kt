package com.oguzdogdu.walliescompose.data.model.topics


import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@kotlinx.parcelize.Parcelize
data class Links(
    @SerializedName("followers") val followers: String?,
    @SerializedName("following") val following: String?,
    @SerializedName("html") val html: String?,
    @SerializedName("likes") val likes: String?,
    @SerializedName("photos") val photos: String?,
    @SerializedName("portfolio") val portfolio: String?,
    @SerializedName("self") val self: String?
):Parcelable