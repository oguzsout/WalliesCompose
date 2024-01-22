package com.oguzdogdu.walliescompose.data.model.searchdto

import com.google.gson.annotations.SerializedName

data class SearchResponseItem(
    @SerializedName("results")
    val results: List<Result>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("total_pages")
    val total_pages: Int
)