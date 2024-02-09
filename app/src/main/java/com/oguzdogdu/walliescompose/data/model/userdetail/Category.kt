package com.oguzdogdu.walliescompose.data.model.userdetail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val pretty_slug: String?,
    val slug: String?
):Parcelable