package com.oguzdogdu.walliescompose.data.model.searchdto.searchuser

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class SearchUsersResponse(
    val results: List<Result>,
    val total: Int,
    val total_pages: Int
):Parcelable