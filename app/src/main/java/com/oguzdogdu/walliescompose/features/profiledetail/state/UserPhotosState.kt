package com.oguzdogdu.walliescompose.features.profiledetail.state

import androidx.compose.runtime.Immutable
import com.oguzdogdu.walliescompose.domain.model.userdetail.UsersPhotos

@Immutable
data class UserPhotosState(
    val loading: Boolean = false,
    val usersPhotos: List<UsersPhotos>? = listOf(),
    val errorMessage: String? = ""
)

