package com.oguzdogdu.walliescompose.domain.model.search.searchuser

import kotlinx.serialization.Serializable

@Serializable
data class SearchUser(
    val id: String?,
    val username: String?,
    val profileImage: String?,
    val name: String?
)
