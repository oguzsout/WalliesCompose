package com.oguzdogdu.walliescompose.data.model.searchdto.searchuser

import kotlinx.serialization.Serializable

@Serializable
data class SearchUsersResponse(
    val results: List<Result>,
    val total: Int,
    val total_pages: Int
)