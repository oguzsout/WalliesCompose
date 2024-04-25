package com.oguzdogdu.walliescompose.features.signup

import android.net.Uri
import androidx.compose.runtime.Stable

@Stable
sealed class SignUpScreenEvent {
    data class SendEmail(val email: String) : SignUpScreenEvent()
    data class SendPassword(val password: String) : SignUpScreenEvent()
    data class SendName(val name: String) : SignUpScreenEvent()
    data class SendSurname(val surname: String) : SignUpScreenEvent()
    data class SendUri(val imageUri: Uri) : SignUpScreenEvent()
    data class ResumeToSignUp(val email: String, val password: String) : SignUpScreenEvent()
    data class CheckPasswordRule(val password: String) : SignUpScreenEvent()
    data object ExecuteFlowOfSignUp : SignUpScreenEvent()
}
