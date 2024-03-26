package com.oguzdogdu.walliescompose.features.authenticateduser.changenameandsurname

sealed class EditUsernameSurnameEvent {
    data class ChangedUserNameAndSurname(val name: String?,val surname: String?) : EditUsernameSurnameEvent()
}
