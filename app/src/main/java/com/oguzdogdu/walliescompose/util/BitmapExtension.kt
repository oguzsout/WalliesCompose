package com.oguzdogdu.walliescompose.util

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.widget.Toast
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asAndroidColorFilter
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import coil.Coil
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.oguzdogdu.walliescompose.features.detail.TypeOfSetWallpaper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

fun Context.setWallpaperFromUrl(
    lifecycleOwner: LifecycleOwner,
    imageUrl: String?,
    place: String?,
    colorFilter: ColorFilter?
) {
    val wallpaperManager = WallpaperManager.getInstance(this)
    val androidColorFilter = colorFilter?.asAndroidColorFilter()
    val imageLoader = Coil.imageLoader(this)

    lifecycleOwner.lifecycleScope.launch {
        val request = ImageRequest.Builder(this@setWallpaperFromUrl)
            .data(imageUrl)
            .allowConversionToBitmap(true)
            .build()

        val result = async { imageLoader.execute(request) }

        val bitmap = result.await().drawable?.toBitmapOrNull()
        if (bitmap != null && androidColorFilter != null) {
            try {
                val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

                val paint = Paint()
                paint.colorFilter = androidColorFilter
                val canvas = Canvas(mutableBitmap)
                canvas.drawBitmap(mutableBitmap, 0f, 0f, paint)

                when (place.orEmpty()) {
                    TypeOfSetWallpaper.LOCK.name ->
                        wallpaperManager
                            .setBitmap(
                                mutableBitmap,
                                null,
                                true,
                                WallpaperManager.FLAG_LOCK
                            )

                    TypeOfSetWallpaper.HOME_AND_LOCK.name ->
                        wallpaperManager.setBitmap(mutableBitmap)

                    TypeOfSetWallpaper.HOME.name ->
                        wallpaperManager
                            .setBitmap(
                                mutableBitmap,
                                null,
                                true,
                                WallpaperManager.FLAG_SYSTEM
                            )
                }
                Toast.makeText(this@setWallpaperFromUrl, "Success", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@setWallpaperFromUrl, e.message, Toast.LENGTH_SHORT).show()
            }
        } else {
            val message = if (bitmap == null) {
                "Failed to load or decode image"
            } else {
                "No color filter provided"
            }
            Toast.makeText(this@setWallpaperFromUrl, message, Toast.LENGTH_SHORT).show()
        }
    }
}
