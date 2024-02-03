package com.oguzdogdu.walliescompose.features.splash

sealed interface SplashScreenEvent {
    data object CheckAuthState : SplashScreenEvent
}