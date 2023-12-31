package com.oguzdogdu.walliescompose.di

import com.oguzdogdu.walliescompose.data.di.Dispatcher
import com.oguzdogdu.walliescompose.data.di.WalliesDispatchers
import com.oguzdogdu.walliescompose.data.repository.WallpaperRepositoryImpl
import com.oguzdogdu.walliescompose.data.service.WallpaperService
import com.oguzdogdu.walliescompose.domain.repository.WallpaperRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
        @Dispatcher(WalliesDispatchers.IO) ioDispatcher: CoroutineDispatcher
    ): WallpaperRepository {
        return WallpaperRepositoryImpl(service, ioDispatcher)
    }
}
