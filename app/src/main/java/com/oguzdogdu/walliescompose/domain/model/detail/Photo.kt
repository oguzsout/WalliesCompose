package com.oguzdogdu.walliescompose.domain.model.detail

import androidx.compose.runtime.Immutable
import com.oguzdogdu.walliescompose.data.model.maindto.Exif
import com.oguzdogdu.walliescompose.data.model.maindto.Location
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class Photo(
    val id: String?,
    val username: String?,
    val portfolio: String?,
    val profileimage:String?,
    val createdAt: String?,
    val desc:String?,
    val urls: String?,
    val views: Double?,
    val downloads: Int?,
    val unsplashProfile: String?,
    val likes: Int?,
    val bio: String?,
    val name: String?,
    val tag: List<String?>?,
    val rawQuality: String?,
    val highQuality: String?,
    val mediumQuality: String?,
    val lowQuality: String?,
    val exif: Exif?,
    val location: Location?,
    val width: Int?,
    val height: Int?,
)
