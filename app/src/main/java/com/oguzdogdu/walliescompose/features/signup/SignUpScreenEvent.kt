package com.oguzdogdu.walliescompose.features.signup

import androidx.compose.runtime.Stable

@Stable
sealed class SignUpScreenEvent {
    data class ResumeToSignUp(val email: String,val password: String) : SignUpScreenEvent()
    data class CheckPasswordRule(val password: String) : SignUpScreenEvent()
}
