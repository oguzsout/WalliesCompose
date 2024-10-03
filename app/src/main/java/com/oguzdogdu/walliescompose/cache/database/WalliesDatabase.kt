package com.oguzdogdu.walliescompose.cache.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.oguzdogdu.walliescompose.cache.dao.FavoriteDao
import com.oguzdogdu.walliescompose.cache.dao.UserPreferencesDao
import com.oguzdogdu.walliescompose.cache.database.WalliesDatabase.Companion.LATEST_VERSION
import com.oguzdogdu.walliescompose.cache.entity.FavoriteImage
import com.oguzdogdu.walliescompose.cache.entity.UserPreferences
import com.oguzdogdu.walliescompose.cache.util.Constants.DB_NAME

@Database(
    version = LATEST_VERSION,
    entities = [FavoriteImage::class, UserPreferences::class],
    exportSchema = true,
    autoMigrations = [
        AutoMigration(
            from = 1,
            to = LATEST_VERSION,
            NameToFirstnameAutoMigrationSpec::class
        )
    ],
)
abstract class WalliesDatabase : RoomDatabase() {

    abstract val favoritesDao: FavoriteDao
    abstract val userPreferencesDao: UserPreferencesDao

    companion object {
        const val LATEST_VERSION = 2

        @Volatile
        private var INSTANCE: WalliesDatabase? = null

        fun getInstance(context: Context): WalliesDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, WalliesDatabase::class.java, DB_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
class NameToFirstnameAutoMigrationSpec : AutoMigrationSpec