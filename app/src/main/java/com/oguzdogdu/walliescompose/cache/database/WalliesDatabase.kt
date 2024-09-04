package com.oguzdogdu.walliescompose.cache.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.oguzdogdu.walliescompose.cache.dao.FavoriteDao
import com.oguzdogdu.walliescompose.cache.dao.UserPreferencesDao
import com.oguzdogdu.walliescompose.cache.entity.FavoriteImage
import com.oguzdogdu.walliescompose.cache.entity.UserPreferences
import com.oguzdogdu.walliescompose.cache.util.Constants.DB_NAME

@Database(version = 2, entities = [FavoriteImage::class, UserPreferences::class])
abstract class WalliesDatabase : RoomDatabase() {

    abstract val favoritesDao: FavoriteDao
    abstract val userPreferencesDao: UserPreferencesDao

    companion object {
        @Volatile
        private var INSTANCE: WalliesDatabase? = null

        fun getInstance(context: Context): WalliesDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, WalliesDatabase::class.java, DB_NAME
        ).build()
    }
}