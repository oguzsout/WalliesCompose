package com.oguzdogdu.walliescompose.features.authenticateduser

import android.net.Uri
import com.oguzdogdu.walliescompose.features.detail.DetailScreenEvent

sealed class AuthenticatedUserEvent {
    data object CheckUserAuth : AuthenticatedUserEvent()
    data object FetchUserInfos : AuthenticatedUserEvent()
    data object SignOut : AuthenticatedUserEvent()
    data class OpenChangeProfileBottomSheet(val isOpen: Boolean) : AuthenticatedUserEvent()
    data class ChangeProfileImage(val photoUri: Uri?) : AuthenticatedUserEvent()
}
