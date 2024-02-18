package com.oguzdogdu.walliescompose.features.login.signinwithemail

import com.oguzdogdu.walliescompose.features.login.LoginScreenEvent

interface SignInWithEmailEvents {
    data object ButtonState : SignInWithEmailEvents
    data class UserSignIn(val email: String, val password: String) : SignInWithEmailEvents
}