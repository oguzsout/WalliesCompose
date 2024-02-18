package com.oguzdogdu.walliescompose.features.login

import androidx.compose.runtime.Stable

@Stable
sealed class LoginState {
    data object Start : LoginState()
    data class Loading(val loading:Boolean) : LoginState()
    data class ErrorSignIn(val errorMessage: String) : LoginState()
    data object UserSignIn : LoginState()
    data object UserNotSignIn : LoginState()
}
