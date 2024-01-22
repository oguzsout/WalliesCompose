package com.oguzdogdu.walliescompose.data.model.searchdto.searchuser


data class Photo(
    val blur_hash: String,
    val created_at: String,
    val id: String,
    val slug: String,
    val updated_at: String,
    val urls: Urls
)