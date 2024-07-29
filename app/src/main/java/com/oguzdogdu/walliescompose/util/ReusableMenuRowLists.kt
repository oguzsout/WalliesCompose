package com.oguzdogdu.walliescompose.util

import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.navigation.utils.WalliesIcons

object ReusableMenuRowLists {
    val newList = listOf(
        ListItem.Header(R.string.settings_general_header),
        ListItem.Content(
            0, description = R.string.theme_text, icon = WalliesIcons.DarkMode, arrow = true
        ),
        ListItem.Content(
            1, R.string.language_title_text, icon = WalliesIcons.Language, arrow = true

        ),
        ListItem.Header(R.string.settings_storage_header),
        ListItem.Content(
            2, R.string.clear_cache_title,icon = WalliesIcons.Cache, arrow = false
        ),
    )
}