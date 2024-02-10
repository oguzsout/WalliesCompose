package com.oguzdogdu.walliescompose.data.model.userdetail

import android.os.Parcelable
import com.oguzdogdu.walliescompose.data.model.userdetail.Urls
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    val blur_hash: String?,
    val created_at: String?,
    val id: String?,
    val slug: String?,
    val updated_at: String?,
    val urls: Urls?
):Parcelable