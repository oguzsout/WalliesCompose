package com.oguzdogdu.walliescompose.features.profiledetail.state

import com.oguzdogdu.walliescompose.domain.model.userdetail.UserCollections

data class UserCollectionState(
    val loading: Boolean = false,
    val usersCollection: List<UserCollections>? = listOf(),
    val errorMessage: String? = ""
)
