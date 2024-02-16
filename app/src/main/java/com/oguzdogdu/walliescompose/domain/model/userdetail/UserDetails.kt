package com.oguzdogdu.walliescompose.domain.model.userdetail

import androidx.compose.runtime.Immutable

@Immutable
data class UserDetails(
    val name: String?,
    val bio: String?,
    val profileImage: String?,
    val postCount: Int?,
    val followingCount: Int?,
    val followersCount: Int?,
    val portfolioUrl: String?,
    val location: String?,
    val username: String?,
    val totalPhotos: Int?,
    val totalCollections: Int?,
    val instagram: String?,
    val twitter: String?,
    val portfolioList: List<String>,
    val portfolio: String?
)
