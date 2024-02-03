package com.oguzdogdu.walliescompose.features.splash

sealed class SplashScreenState {
    data object StartFlow : SplashScreenState()
    data object UserSignedIn : SplashScreenState()
    data object UserNotSigned : SplashScreenState()
}
