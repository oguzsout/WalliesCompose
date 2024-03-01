package com.oguzdogdu.walliescompose.features.login.signinwithemail

sealed interface SignInWithEmailEvents {
    data object ButtonState : SignInWithEmailEvents
    data class UserSignIn(val email: String, val password: String) : SignInWithEmailEvents
}