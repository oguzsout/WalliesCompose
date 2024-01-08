package com.oguzdogdu.walliescompose.di

import android.content.Context
import androidx.compose.runtime.Stable
import com.oguzdogdu.walliescompose.WalliesApplication
import com.oguzdogdu.walliescompose.util.ILocaleHelper
import com.oguzdogdu.walliescompose.util.LocaleHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Stable
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): WalliesApplication {
        return app as WalliesApplication
    }
    @Provides
    @Singleton
    fun provideLocaleHelper(@ApplicationContext context: Context): ILocaleHelper {
        return LocaleHelper(context)
    }
}