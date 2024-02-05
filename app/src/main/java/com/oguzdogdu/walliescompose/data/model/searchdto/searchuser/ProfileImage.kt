package com.oguzdogdu.walliescompose.data.model.searchdto.searchuser

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class ProfileImage(
    val large: String,
    val medium: String,
    val small: String
):Parcelable