package com.oguzdogdu.walliescompose.data.model.userdetail

import android.os.Parcelable
import com.oguzdogdu.walliescompose.data.model.userdetail.Category
import com.oguzdogdu.walliescompose.data.model.userdetail.Subcategory
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ancestry(
    val category: Category?,
    val subcategory: Subcategory?,
):Parcelable