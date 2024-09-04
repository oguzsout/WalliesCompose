package com.oguzdogdu.walliescompose.cache.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userPreferences")
data class UserPreferences(
    @PrimaryKey(autoGenerate = true) val searchKeywordId: Int = 0,
    val keyword: String?
)

fun UserPreferences.toDomain() =
    com.oguzdogdu.walliescompose.domain.model.userpreferences.UserPreferences(
        id = searchKeywordId,
        keyword = keyword
    )