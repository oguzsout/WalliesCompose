package com.oguzdogdu.walliescompose.data.model.collection

import com.google.gson.annotations.SerializedName
import com.oguzdogdu.walliescompose.data.model.maindto.Link
import com.oguzdogdu.walliescompose.data.model.maindto.Photo
import com.oguzdogdu.walliescompose.data.model.maindto.User
import com.oguzdogdu.walliescompose.domain.model.collections.WallpaperCollections
import com.oguzdogdu.walliescompose.domain.model.userdetail.UserCollections
import kotlinx.serialization.Serializable

@Serializable
data class CollectionResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("published_at")
    val published_at: String?,
    @SerializedName("updated_at")
    val updated_at: String?,
    @SerializedName("total_photos")
    val total_photos: Int,
    @SerializedName("cover_photo")
    val cover_photo: Photo?,
    @SerializedName("preview_photos")
    val preview_photos: List<Photo>?,
    @SerializedName("user")
    val user: User?,
    @SerializedName("links")
    val links: Link?,
)

fun CollectionResponse.toCollectionDomain() =
    WallpaperCollections(
        id = id,
        title = title,
        photo = cover_photo?.urls?.regular,
        desc = description,
        likes = cover_photo?.likes,
        updatedAt = updated_at
    )
fun CollectionResponse.toUserCollection() = UserCollections(
    id = id,
    title = title,
    photo = cover_photo?.urls?.regular,
    desc = description,
    likes = cover_photo?.likes
)
