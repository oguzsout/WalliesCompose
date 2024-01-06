package com.oguzdogdu.walliescompose.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

@Immutable
data class MenuRow(
    @StringRes val titleRes: Int? = null,
    @DrawableRes val icon: Int? = null
)
