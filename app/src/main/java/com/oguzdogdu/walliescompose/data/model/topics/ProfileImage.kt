package com.oguzdogdu.walliescompose.data.model.topics


import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@kotlinx.parcelize.Parcelize
data class ProfileImage(
    @SerializedName("large") val large: String?,
    @SerializedName("medium") val medium: String?,
    @SerializedName("small") val small: String?
):Parcelable