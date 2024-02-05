package com.oguzdogdu.walliescompose.data.model.maindto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
@kotlinx.parcelize.Parcelize
data class Sponsorship(
    @SerializedName("sponsor")
    val sponsor: Sponsor?,
    @SerializedName("tagline")
    val tagline: String?,
    @SerializedName("tagline_url")
    val taglineUrl: String?
):Parcelable