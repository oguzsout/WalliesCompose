package com.oguzdogdu.walliescompose.features.signup

import androidx.compose.runtime.Stable
import com.oguzdogdu.walliescompose.features.login.signinwithemail.SignInWithEmailEvents

@Stable
sealed class SignUpScreenEvent {
    data object ExecuteValidation: SignUpScreenEvent()
    data class ResumeToSignUp(val email: String,val password: String) : SignUpScreenEvent()
}
