package com.oguzdogdu.walliescompose.features.settings

sealed interface SettingsScreenEvent {
    data class OpenThemeDialog(val open:Boolean = false): SettingsScreenEvent
}