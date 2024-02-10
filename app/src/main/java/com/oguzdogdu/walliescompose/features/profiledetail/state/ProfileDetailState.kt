package com.oguzdogdu.walliescompose.features.profiledetail.state

import com.oguzdogdu.walliescompose.domain.model.userdetail.UserDetails

data class ProfileDetailState(
    val loading: Boolean = false,
    val userDetails: UserDetails? = null,
    val errorMessage: String? = null
)

