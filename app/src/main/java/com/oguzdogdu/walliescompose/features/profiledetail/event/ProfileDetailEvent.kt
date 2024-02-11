package com.oguzdogdu.walliescompose.features.profiledetail.event

sealed class ProfileDetailEvent {
    data object FetchUserDetailInfos : ProfileDetailEvent()
}
