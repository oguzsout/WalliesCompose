package com.oguzdogdu.walliescompose.features.authenticateduser.changepassword

sealed class ChangePasswordScreenEvent {
    data class UserPassword(
        val password: String? = null
    ) : ChangePasswordScreenEvent()
    data object ButtonState : ChangePasswordScreenEvent()

}
