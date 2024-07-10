package com.oguzdogdu.walliescompose.data.model.photodetail

import com.google.gson.annotations.SerializedName
import com.oguzdogdu.walliescompose.data.model.maindto.Exif
import com.oguzdogdu.walliescompose.data.model.maindto.Link
import com.oguzdogdu.walliescompose.data.model.maindto.Location
import com.oguzdogdu.walliescompose.data.model.maindto.Sponsorship
import com.oguzdogdu.walliescompose.data.model.maindto.Urls
import com.oguzdogdu.walliescompose.data.model.maindto.User
import com.oguzdogdu.walliescompose.data.model.userdetail.Meta
import com.oguzdogdu.walliescompose.domain.model.detail.Photo
import kotlinx.serialization.Serializable

@Serializable
data class PhotoDetailResponse(
    @SerializedName("alt_description") val altDescription: String?,
    @SerializedName("asset_type") val assetType: String?,
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
    @SerializedName("exif") val exif: Exif?,
    @SerializedName("meta") val meta: Meta?,
    @SerializedName("location") val location: Location?,
    @SerializedName("width") val width: Int?,
    @SerializedName("views") val views: Double?,
    @SerializedName("downloads") val downloads: Int?
)

@Serializable
data class Tags(
    val type: String?,
    val title: String?,
)
fun PhotoDetailResponse.convertList() = tags.map { it?.title }
fun PhotoDetailResponse.toDomainModelPhoto() = Photo(
    id = id,
    username = user?.username,
    portfolio = user?.portfolioUrl,
    profileimage = user?.profileImage?.large,
    createdAt = createdAt,
    desc = altDescription,
    urls = urls?.regular,
    views = views,
    downloads = downloads,
    unsplashProfile = user?.links?.html,
    likes = likes,
    bio = user?.bio,
    name = user?.name,
    tag = this.convertList(),
    rawQuality = urls?.raw,
    highQuality = urls?.full,
    mediumQuality = urls?.regular,
    lowQuality = urls?.small,
    exif = exif,
    location = location,
    width = width,
    height = height
)