package com.oguzdogdu.walliescompose.data.model.searchdto.searchuser

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class Urls(
    val full: String,
    val raw: String,
    val regular: String,
    val small: String,
    val small_s3: String,
    val thumb: String
):Parcelable