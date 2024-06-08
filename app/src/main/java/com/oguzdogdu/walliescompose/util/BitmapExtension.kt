package com.oguzdogdu.walliescompose.util

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.widget.Toast
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asAndroidColorFilter
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import coil.Coil
import coil.ImageLoader
import coil.request.ImageRequest
import com.oguzdogdu.walliescompose.features.detail.TypeOfSetWallpaper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

suspend fun Context.setWallpaperFromUrl(
    lifecycleOwner: LifecycleOwner,
    imageUrl: String?,
    place: String?,
    colorFilter: ColorFilter?,
    onSuccess: () -> Unit,
    onError: () -> Unit
) {
    val wallpaperManager = WallpaperManager.getInstance(this)
    val androidColorFilter = colorFilter?.asAndroidColorFilter()
    val imageLoader = Coil.imageLoader(this)
    val bitmap = loadImageBitmap(this@setWallpaperFromUrl, imageLoader, imageUrl)

    lifecycleOwner.lifecycleScope.launch {
        try {
            when {
                bitmap != null && androidColorFilter != null -> {
                    val filteredBitmap = applyColorFilter(bitmap, androidColorFilter)
                    setWallpaper(wallpaperManager, filteredBitmap, place.orEmpty())
                    onSuccess.invoke()
                }
                else -> {
                    onError.invoke()
                }
            }
        } catch (e: Exception) {
            showToast(e.message ?: "Unknown error")
        }
    }.invokeOnCompletion {

    }
}

private suspend fun loadImageBitmap(
    context: Context,
    imageLoader: ImageLoader,
    imageUrl: String?
): Bitmap? {
    return withContext(Dispatchers.IO) {
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .build()

        val result = async { imageLoader.execute(request) }
        result.await().drawable?.toBitmapOrNull()
    }
}

private fun applyColorFilter(bitmap: Bitmap, colorFilter: android.graphics.ColorFilter?): Bitmap {
    val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
    val paint = Paint()
    paint.colorFilter = colorFilter
    val canvas = Canvas(mutableBitmap)
    canvas.drawBitmap(mutableBitmap, 0f, 0f, paint)
    return mutableBitmap
}

private fun Context.setWallpaper(
    wallpaperManager: WallpaperManager,
    bitmap: Bitmap,
    place: String
) {
    when (place) {
        TypeOfSetWallpaper.LOCK.name -> wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
        TypeOfSetWallpaper.HOME_AND_LOCK.name -> wallpaperManager.setBitmap(bitmap)
        TypeOfSetWallpaper.HOME.name -> wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM)
        else -> wallpaperManager.setBitmap(bitmap)
    }
}

private fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}