package com.oguzdogdu.walliescompose.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
data class MenuRow(
    val category:String? = null,
    @StringRes val titleRes: Int? = null,
    @DrawableRes val icon: Int? = null
)
