package com.oguzdogdu.walliescompose.data.model.userdetail

import kotlinx.serialization.Serializable

@Serializable
data class Ancestry(
    val category: Category?,
    val subcategory: Subcategory?,
)