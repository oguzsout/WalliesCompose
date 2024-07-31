package com.oguzdogdu.walliescompose.data.model.maindto

import com.google.gson.annotations.SerializedName
import com.oguzdogdu.walliescompose.domain.model.latest.LatestImage
import com.oguzdogdu.walliescompose.domain.model.popular.PopularImage
import com.oguzdogdu.walliescompose.domain.model.random.RandomImage
import kotlinx.serialization.Serializable

@Serializable
data class UnsplashResponseItem(
    @SerializedName("alt_description") val altDescription: String?,
    @SerializedName("blur_hash") val blurHash: String?,
    @SerializedName("color") val color: String?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("height") val height: Int?,
    @SerializedName("id") val id: String?,
    @SerializedName("liked_by_user") val likedByUser: Boolean?,
    @SerializedName("likes") val likes: Int?,
    @SerializedName("links") val links: Link?,
    @SerializedName("promoted_at") val promotedAt: String?,
    @SerializedName("sponsorship") val sponsorship: Sponsorship?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("urls") val urls: Urls?,
    @SerializedName("tags") val tags: List<Tags?>,
    @SerializedName("user") val user: User?,
    @SerializedName("width") val width: Int?,
    @SerializedName("views") val views: Double?,
    @SerializedName("downloads") val downloads: Int?
)

@Serializable
data class Tags(
    val type: String?,
    val title: String?,
)

fun UnsplashResponseItem.toDomainModelPopular() =
    PopularImage(id = id, url = urls?.regular, imageDesc = altDescription)

fun UnsplashResponseItem.toDomainModelLatest() =
    LatestImage(id = id, url = urls?.regular, imageDesc = altDescription)

fun UnsplashResponseItem.toDomainModelRandom() =
    RandomImage(
        id = id,
        url = urls?.regular,
        imageDesc = altDescription,
        color = color,
        username = user?.name,
        userImage = user?.profileImage?.medium
    )