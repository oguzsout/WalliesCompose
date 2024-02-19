package com.oguzdogdu.walliescompose.features.login.forgotpassword

sealed class ForgotPasswordScreenEvent {
    data class SendEmail(
        val email: String? = null
    ) : ForgotPasswordScreenEvent()
    object ButtonState : ForgotPasswordScreenEvent()
}
