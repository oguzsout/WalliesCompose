package com.oguzdogdu.walliescompose.data.model.topics


import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@kotlinx.parcelize.Parcelize
data class PreviewPhoto(
    @SerializedName("blur_hash") val blurHash: String?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("id") val id: String?,
    @SerializedName("slug") val slug: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("urls") val urls: Urls?
):Parcelable