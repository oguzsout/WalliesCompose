package com.oguzdogdu.walliescompose.features.authenticateduser.changeemail

sealed class ChangeUserEmailEvent {
    data object ButtonState : ChangeUserEmailEvent()
    data object ChangedEmail : ChangeUserEmailEvent()
}
