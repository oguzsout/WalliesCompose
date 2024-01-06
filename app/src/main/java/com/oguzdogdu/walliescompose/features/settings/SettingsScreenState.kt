package com.oguzdogdu.walliescompose.features.settings

data class SettingsScreenState(
    val openThemeDialog:Boolean=false,
    val setThemeValue: String? = null,
    val getThemeValue: String? = null,
)
