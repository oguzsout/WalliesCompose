package com.oguzdogdu.walliescompose.data.model.userdetail

import com.oguzdogdu.walliescompose.domain.model.userdetail.UserDetails
import kotlinx.serialization.Serializable

@Serializable
data class UserDetailResponse(
    val accepted_tos: Boolean,
    val allow_messages: Boolean,
    val bio: String?,
    val downloads: Int?,
    val first_name: String?,
    val followed_by_user: Boolean,
    val followers_count: Int?,
    val following_count: Int?,
    val for_hire: Boolean,
    val id: String?,
    val instagram_username: String?,
    val last_name: String?,
    val links: Links?,
    val location: String?,
    val meta: Meta?,
    val name: String?,
    val numeric_id: Int?,
    val photos: List<Photo>?,
    val portfolio_url: String?,
    val profile_image: ProfileImage?,
    val social: Social?,
    val tags: Tags?,
    val total_collections: Int?,
    val total_likes: Int?,
    val total_photos: Int?,
    val twitter_username: String?,
    val updated_at: String?,
    val username: String?
)

fun UserDetailResponse.toDomain() : UserDetails {
    val nonNullPortfolioList = listOfNotNull(
        social?.portfolio_url,
        social?.twitter_username,
        social?.instagram_username
    )
    return UserDetails(
        name = name,
        bio = bio,
        profileImage = profile_image?.large,
        postCount = total_photos,
        followersCount = followers_count,
        followingCount = following_count,
        portfolioUrl = portfolio_url,
        location = location,
        username = username,
        totalPhotos = total_photos,
        totalCollections = total_collections,
        instagram = social?.instagram_username,
        twitter = social?.twitter_username,
        portfolioList = nonNullPortfolioList,
        portfolio = social?.portfolio_url,
        forHire = for_hire
    )
}