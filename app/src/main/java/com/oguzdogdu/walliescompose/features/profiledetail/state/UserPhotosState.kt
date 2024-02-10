package com.oguzdogdu.walliescompose.features.profiledetail.state

import com.oguzdogdu.walliescompose.domain.model.userdetail.UsersPhotos

data class UserPhotosState(
    val loading: Boolean = false,
    val usersPhotos: List<UsersPhotos>? = listOf(),
    val errorMessage: String? = ""
)

