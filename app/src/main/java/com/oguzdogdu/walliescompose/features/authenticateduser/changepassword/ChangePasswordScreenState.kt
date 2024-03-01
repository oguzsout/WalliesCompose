package com.oguzdogdu.walliescompose.features.authenticateduser.changepassword

sealed class ChangePasswordScreenState {
    data class Loading(val isLoading:Boolean) : ChangePasswordScreenState()
    data class PasswordChangeError(val errorMessage: String?) : ChangePasswordScreenState()
    data class PasswordChangeStatus(val status: Boolean?) : ChangePasswordScreenState()
    data class ButtonEnabled(val isEnabled: Boolean) : ChangePasswordScreenState()
}
