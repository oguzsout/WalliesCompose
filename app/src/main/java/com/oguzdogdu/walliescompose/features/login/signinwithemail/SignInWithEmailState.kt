package com.oguzdogdu.walliescompose.features.login.signinwithemail

import androidx.compose.runtime.Stable

@Stable
sealed interface SignInWithEmailState {
    data class Loading(val loading:Boolean) : SignInWithEmailState
    data class ButtonEnabled(val isEnabled: Boolean) : SignInWithEmailState
    data class ErrorSignIn(val errorMessage: String) : SignInWithEmailState
    data object UserSignIn : SignInWithEmailState
    data object UserNotSignIn : SignInWithEmailState
}