package com.oguzdogdu.walliescompose.data.model.userdetail

import android.os.Parcelable
import com.oguzdogdu.walliescompose.data.model.userdetail.Source
import kotlinx.parcelize.Parcelize

@Parcelize
data class Aggregated(
    val source: Source?,
    val title: String?,
    val type: String?
):Parcelable