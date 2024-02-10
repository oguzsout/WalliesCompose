package com.oguzdogdu.walliescompose.features.profiledetail.event

sealed class ProfileDetailEvent {
    data object FetchUserDetailInfos : ProfileDetailEvent()
    data object FetchUserPhotosList : ProfileDetailEvent()
    data object FetchUserCollectionsList : ProfileDetailEvent()
}
