package com.oguzdogdu.walliescompose.features.profiledetail.state

sealed interface ProfileDetailUIState {
    data object Loading: ProfileDetailUIState
    data object ReadyForShown: ProfileDetailUIState
    data class Error(val errorMessage:String? = null) : ProfileDetailUIState
}