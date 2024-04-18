package com.oguzdogdu.walliescompose.data.model.maindto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.oguzdogdu.walliescompose.domain.model.detail.Photo
import com.oguzdogdu.walliescompose.domain.model.latest.LatestImage
import com.oguzdogdu.walliescompose.domain.model.popular.PopularImage

@kotlinx.parcelize.Parcelize
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
):Parcelable

@kotlinx.parcelize.Parcelize
data class Tags(
    val type: String?,
    val title: String?,
):Parcelable
fun UnsplashResponseItem.convertList() = tags.map { it?.title }
fun UnsplashResponseItem.toDomainModelPopular() = PopularImage(id = id, url = urls?.regular, imageDesc = altDescription)
fun UnsplashResponseItem.toDomainModelLatest() = LatestImage(id = id, url = urls?.regular, imageDesc = altDescription)
fun UnsplashResponseItem.toDomainModelPhoto() = Photo(
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
    location = user?.location,
    rawQuality = urls?.raw,
    highQuality = urls?.full,
    mediumQuality = urls?.regular,
    lowQuality = urls?.small
)