package com.oguzdogdu.walliescompose.util

import android.app.WallpaperManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.lifecycle.LifecycleOwner
import coil.Coil
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.oguzdogdu.walliescompose.features.detail.TypeOfSetWallpaper
import kotlinx.coroutines.Dispatchers

fun Context.setWallpaperFromUrl(lifecycleOwner: LifecycleOwner, imageUrl: String?, place: String?) {
    val imageLoader = Coil.imageLoader(this)
    val request = ImageRequest.Builder(this)
        .data(imageUrl)
        .transformationDispatcher(Dispatchers.Main.immediate)
        .lifecycle(lifecycleOwner)
        .allowConversionToBitmap(true)
        .memoryCachePolicy(CachePolicy.READ_ONLY)
        .target(
            onSuccess = { result ->
                val wallpaperManager = WallpaperManager.getInstance(this)
                try {
                    when (place.orEmpty()) {
                        TypeOfSetWallpaper.LOCK.name ->
                            wallpaperManager
                                .setBitmap(
                                    result.toBitmapOrNull(),
                                    null,
                                    true,
                                    WallpaperManager.FLAG_LOCK
                                )

                        TypeOfSetWallpaper.HOME_AND_LOCK.name ->
                            wallpaperManager.setBitmap(result.toBitmapOrNull())

                        TypeOfSetWallpaper.HOME.name ->
                            wallpaperManager.setBitmap(
                                result.toBitmapOrNull(),
                                null,
                                true,
                                WallpaperManager.FLAG_SYSTEM
                            )
                    }
                    Toast.makeText(this,"Success", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this,e.message, Toast.LENGTH_SHORT).show()
                }
            },
            onError = { error ->
                Toast.makeText(this,"Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
        )
        .build()
    imageLoader.enqueue(request)
}