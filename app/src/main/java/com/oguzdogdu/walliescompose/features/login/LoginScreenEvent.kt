package com.oguzdogdu.walliescompose.features.login

import androidx.compose.runtime.Stable

@Stable
sealed class LoginScreenEvent {
    data class GoogleButton(val idToken: String?) : LoginScreenEvent()
}
