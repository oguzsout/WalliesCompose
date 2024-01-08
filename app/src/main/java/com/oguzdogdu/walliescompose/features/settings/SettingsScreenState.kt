package com.oguzdogdu.walliescompose.features.settings

data class SettingsScreenState(
    val openThemeDialog: Boolean = false,
    val openLanguageDialog: Boolean = false,
    val cache: Boolean = false,
    val getThemeValue: String? = null,
    val getLanguageValue: String? = null,
)
