package com.oguzdogdu.walliescompose.util

import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.navigation.utils.WalliesIcons

object ReusableMenuRowLists {
    val generalOptionList = mutableListOf(
        MenuRow(
            category = "General",
            icon = WalliesIcons.DarkMode,
            titleRes = R.string.theme_text
        ),
        MenuRow(
            category = "General",
            icon = WalliesIcons.Language,
            titleRes = R.string.language_title_text
        )
    )

    val storageOptionList = mutableListOf(
        MenuRow(
            category = "Storage",
            icon = WalliesIcons.Cache,
            titleRes = R.string.clear_cache_title
        )
    )
}