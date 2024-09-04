package com.oguzdogdu.walliescompose.cache.di

import android.content.Context
import com.oguzdogdu.walliescompose.cache.dao.FavoriteDao
import com.oguzdogdu.walliescompose.cache.dao.UserPreferencesDao
import com.oguzdogdu.walliescompose.cache.database.WalliesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): WalliesDatabase {
        return WalliesDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideFavoritesDao(database: WalliesDatabase): FavoriteDao {
        return database.favoritesDao
    }

    @Provides
    @Singleton
    fun provideUserPreferencesDao(database: WalliesDatabase): UserPreferencesDao {
        return database.userPreferencesDao
    }
}