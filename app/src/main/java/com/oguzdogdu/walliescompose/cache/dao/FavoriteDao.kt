package com.oguzdogdu.walliescompose.cache.dao

import androidx.room.*
import com.oguzdogdu.walliescompose.cache.entity.FavoriteImage
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorites(favorite: FavoriteImage)

    @Query("SELECT * FROM favorites")
    fun getFavorites(): Flow<List<FavoriteImage>>

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteImage)
}