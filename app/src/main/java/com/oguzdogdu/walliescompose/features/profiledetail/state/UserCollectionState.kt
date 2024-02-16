package com.oguzdogdu.walliescompose.features.profiledetail.state

import androidx.compose.runtime.Immutable
import com.oguzdogdu.walliescompose.domain.model.userdetail.UserCollections

@Immutable
data class UserCollectionState(
    val loading: Boolean = false,
    val usersCollection: List<UserCollections>? = listOf(),
    val errorMessage: String? = ""
)
