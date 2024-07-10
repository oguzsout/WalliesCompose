package com.oguzdogdu.walliescompose.data.model.maindto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Position(
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?
)