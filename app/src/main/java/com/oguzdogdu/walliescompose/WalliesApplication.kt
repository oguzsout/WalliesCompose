package com.oguzdogdu.walliescompose

import android.app.Application
import android.content.Context
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.oguzdogdu.walliescompose.domain.model.popular.PopularImage
import com.oguzdogdu.walliescompose.features.settings.ThemeValues
import com.oguzdogdu.walliescompose.util.LocaleHelper
import dagger.hilt.android.HiltAndroidApp

@Stable
@HiltAndroidApp
class WalliesApplication : Application(), ImageLoaderFactory {
    val theme = mutableStateOf("")
    val language = mutableStateOf("")
    val imagesList = mutableListOf<String?>()
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .networkObserverEnabled(true)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache(
                DiskCache.Builder().directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(50 * 1024 * 1024).build()
            )
            .build()
    }
}