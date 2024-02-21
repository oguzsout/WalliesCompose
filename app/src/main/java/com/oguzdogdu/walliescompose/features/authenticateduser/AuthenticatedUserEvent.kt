package com.oguzdogdu.walliescompose.features.authenticateduser

sealed class AuthenticatedUserEvent {
    data object CheckUserAuth : AuthenticatedUserEvent()
    data object FetchUserInfos : AuthenticatedUserEvent()
    data object SignOut : AuthenticatedUserEvent()
}
