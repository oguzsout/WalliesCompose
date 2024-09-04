package com.oguzdogdu.walliescompose.cache.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.oguzdogdu.walliescompose.cache.entity.UserPreferences
import kotlinx.coroutines.flow.Flow

@Dao
interface UserPreferencesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecentSearchKeys(userPreferences: UserPreferences)

    @Query("SELECT * FROM userPreferences")
    fun getRecentSearchKeys(): Flow<List<UserPreferences>>

    @Query("DELETE FROM userPreferences WHERE keyword = :keyword")
    suspend fun deleteRecentSearchKeyByKeyword(keyword: String)
}