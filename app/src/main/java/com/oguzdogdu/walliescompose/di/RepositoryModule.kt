package com.oguzdogdu.walliescompose.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oguzdogdu.walliescompose.cache.dao.FavoriteDao
import com.oguzdogdu.walliescompose.data.repository.AppSettingsRepositoryImpl
import com.oguzdogdu.walliescompose.data.repository.WallpaperRepositoryImpl
import com.oguzdogdu.walliescompose.data.di.Dispatcher
import com.oguzdogdu.walliescompose.data.di.WalliesDispatchers
import com.oguzdogdu.walliescompose.data.repository.UnsplashUserRepositoryImpl
import com.oguzdogdu.walliescompose.data.repository.UserAuthenticationRepositoryImpl
import com.oguzdogdu.walliescompose.data.service.UnsplashUserService
import com.oguzdogdu.walliescompose.data.service.WallpaperService
import com.oguzdogdu.walliescompose.domain.repository.AppSettingsRepository
import com.oguzdogdu.walliescompose.domain.repository.UnsplashUserRepository
import com.oguzdogdu.walliescompose.domain.repository.UserAuthenticationRepository
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
        return WallpaperRepositoryImpl(service, dao, ioDispatcher)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): UserAuthenticationRepository {
        return UserAuthenticationRepositoryImpl(auth, firestore)
    }

    @Provides
    @Singleton
    fun provideUnsplashUserRepository(
        service: UnsplashUserService,
        @Dispatcher(WalliesDispatchers.IO) ioDispatcher: CoroutineDispatcher
    ): UnsplashUserRepository {
        return UnsplashUserRepositoryImpl(service, ioDispatcher)
    }

    @Singleton
    @Provides
    fun providesDatastoreRepo(
        @ApplicationContext context: Context
    ): AppSettingsRepository = AppSettingsRepositoryImpl(context)
}
