package com.oguzdogdu.walliescompose.data.model.topics

import com.google.gson.annotations.SerializedName
import com.oguzdogdu.walliescompose.domain.model.topics.Topics
import kotlinx.serialization.Serializable

@Serializable
data class TopicsResponseItem(
    @SerializedName("cover_photo") val coverPhoto: CoverPhoto?,
    @SerializedName("description") val description: String?,
    @SerializedName("ends_at") val endsAt: String?,
    @SerializedName("featured") val featured: Boolean?,
    @SerializedName("id") val id: String?,
    @SerializedName("links") val links: Links?,
    @SerializedName("owners") val owners: List<Owner>?,
    @SerializedName("preview_photos") val previewPhotos: List<PreviewPhoto>?,
    @SerializedName("published_at") val publishedAt: String?,
    @SerializedName("slug") val slug: String?,
    @SerializedName("starts_at") val startsAt: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("total_photos") val totalPhotos: Int?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("visibility") val visibility: String?
)

fun TopicsResponseItem.toDomainTopics() =
    Topics(id = id, title = slug, titleBackground = previewPhotos?.first()?.urls?.regular)