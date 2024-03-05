package com.oguzdogdu.walliescompose.features.authenticateduser.changeemail

sealed class ChangeEmailScreenState {
    data object Start : ChangeEmailScreenState()
    data class Loading(val isLoading:Boolean) : ChangeEmailScreenState()
    data class ChangedEmailError(val errorMessage: String?) : ChangeEmailScreenState()
    data class ChangedEmail(val emailChanged:String) : ChangeEmailScreenState()
    data class ButtonEnabled(val isEnabled: Boolean) : ChangeEmailScreenState()
}
