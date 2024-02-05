package com.oguzdogdu.walliescompose.data.model.searchdto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.oguzdogdu.walliescompose.domain.model.search.SearchPhoto
import com.oguzdogdu.walliescompose.data.model.maindto.Link
import com.oguzdogdu.walliescompose.data.model.maindto.User
import com.oguzdogdu.walliescompose.data.model.searchdto.searchuser.Urls
@kotlinx.parcelize.Parcelize
data class Result(
    @SerializedName("alt_description")
    val alt_description: String?,
    @SerializedName("color")
    val color: String?,
    @SerializedName("created_at")
    val created_at: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("likes")
    val likes: Int?,
    @SerializedName("links")
    val links: Link?,
    @SerializedName("promoted_at")
    val promoted_at: String?,
    @SerializedName("updated_at")
    val updated_at: String?,
    @SerializedName("urls")
    val urls: Urls?,
    @SerializedName("user")
    val user: User?,
):Parcelable

fun Result.toDomainSearch() = SearchPhoto(
    id = id,
    url = urls?.small
)