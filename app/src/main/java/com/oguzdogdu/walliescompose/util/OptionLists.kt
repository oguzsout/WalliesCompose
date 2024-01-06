package com.oguzdogdu.walliescompose.util

import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.navigation.utils.WalliesIcons

object OptionLists {
    val appOptionsList = listOf(
        MenuRow(
            icon = WalliesIcons.DarkMode,
            titleRes = R.string.theme_text
        ),
        MenuRow(
            icon = WalliesIcons.Language,
            titleRes = R.string.language_title_text
        ),
        MenuRow(
            icon = WalliesIcons.Cache,
            titleRes = R.string.clear_cache_title
        )
    )
}
