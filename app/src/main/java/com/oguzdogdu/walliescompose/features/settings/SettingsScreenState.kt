package com.oguzdogdu.walliescompose.features.settings

data class SettingsScreenState(
    val openThemeDialog: Boolean = false,
    val openLanguageDialog: Boolean = false,
    val showSnackBar: Boolean? = null,
    val getThemeValue: String? = null,
    val getLanguageValue: String? = null,
)
