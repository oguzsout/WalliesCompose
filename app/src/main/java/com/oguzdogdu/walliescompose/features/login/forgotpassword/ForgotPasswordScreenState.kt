package com.oguzdogdu.walliescompose.features.login.forgotpassword

sealed class ForgotPasswordScreenState {
    data class ButtonEnabled(val isEnabled: Boolean) : ForgotPasswordScreenState()
    data class SendEmailError(val error: String) : ForgotPasswordScreenState()
    data class Loading(val loading: Boolean) : ForgotPasswordScreenState()
    data class ProcessStat(val isCompleted:Boolean) : ForgotPasswordScreenState()
}
