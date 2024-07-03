package com.oguzdogdu.walliescompose.features.authenticateduser.changeemail

data class ChangeEmailScreenState(
    val isLoading: Boolean = false,
    var errorMessage: String? = null,
    val emailChanged: String? = null,
    val isEnabled: Boolean = false
)
