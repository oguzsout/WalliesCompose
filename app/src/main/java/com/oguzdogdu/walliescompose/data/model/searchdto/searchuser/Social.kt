package com.oguzdogdu.walliescompose.data.model.searchdto.searchuser

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class Social(
    val instagram_username: String,
    val paypal_email: String,
    val portfolio_url: String,
    val twitter_username: String
):Parcelable