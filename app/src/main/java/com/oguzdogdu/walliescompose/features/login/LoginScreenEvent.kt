package com.oguzdogdu.walliescompose.features.login

sealed class LoginScreenEvent {
    data object ButtonState : LoginScreenEvent()
    data class GoogleButton(val idToken: String?) : LoginScreenEvent()
    data class UserSignIn(val email: String, val password: String) : LoginScreenEvent()
}
