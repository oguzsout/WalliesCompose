package com.oguzdogdu.walliescompose.data.model.searchdto.searchuser

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class Links(
    val followers: String,
    val following: String,
    val html: String,
    val likes: String,
    val photos: String,
    val portfolio: String,
    val self: String
):Parcelable