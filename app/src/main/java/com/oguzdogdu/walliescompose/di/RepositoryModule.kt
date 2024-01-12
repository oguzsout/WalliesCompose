package com.oguzdogdu.walliescompose.di

import android.content.Context
import com.oguzdogdu.walliescompose.cache.dao.FavoriteDao
import com.oguzdogdu.walliescompose.data.repository.AppSettingsRepositoryImpl
import com.oguzdogdu.walliescompose.data.repository.WallpaperRepositoryImpl
import com.oguzdogdu.walliescompose.data.di.Dispatcher
import com.oguzdogdu.walliescompose.data.di.WalliesDispatchers
import com.oguzdogdu.walliescompose.data.service.WallpaperService
import com.oguzdogdu.walliescompose.domain.repository.AppSettingsRepository
import com.oguzdogdu.walliescompose.domain.repository.WallpaperRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideWallpaperRepository(
        service: WallpaperService,
        dao: FavoriteDao,
        @Dispatcher(WalliesDispatchers.IO) ioDispatcher: CoroutineDispatcher
    ): WallpaperRepository {
        return WallpaperRepositoryImpl(service, dao,ioDispatcher)
    }
    @Singleton
    @Provides
    fun providesDatastoreRepo(
        @ApplicationContext context: Context
    ): AppSettingsRepository = AppSettingsRepositoryImpl(context)
}
