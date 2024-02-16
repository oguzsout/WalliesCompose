package com.oguzdogdu.walliescompose.features.profiledetail.state

import androidx.compose.runtime.Immutable
import com.oguzdogdu.walliescompose.domain.model.userdetail.UserDetails

@Immutable
data class ProfileDetailState(
    val loading: Boolean = false,
    val userDetails: UserDetails? = null,
    val errorMessage: String? = null
)

