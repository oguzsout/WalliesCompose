package com.oguzdogdu.walliescompose.features.main

sealed interface MainScreenEvent {
    data object ThemeChanged : MainScreenEvent
    data object LanguageChanged : MainScreenEvent
}