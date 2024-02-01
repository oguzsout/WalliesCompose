package com.oguzdogdu.walliescompose.features.login

sealed class LoginState {
    data object Start : LoginState()
    data object Loading : LoginState()
    data class ButtonEnabled(val isEnabled: Boolean) : LoginState()
    data class ErrorSignIn(val errorMessage: String) : LoginState()
    data object UserSignIn : LoginState()
    data object UserNotSignIn : LoginState()
}
