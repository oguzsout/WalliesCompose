package com.oguzdogdu.walliescompose.util

import android.app.DownloadManager
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import java.io.File
import java.io.IOException

fun Context.downloadImage(url: String, directoryName: String, fileName: String): Boolean {
    val directory = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
        directoryName
    )
    if (!directory.exists()) {
        directory.mkdirs()
    }
    val file = File(directory, fileName)

    if (file.exists()) {
        return false
    }

    try {
        val downloadManager =
            this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadUri = Uri.parse(url)
        val request = DownloadManager.Request(downloadUri).apply {
            setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE
            )
                .setMimeType("image/*")
                .setAllowedOverRoaming(true)
                .setNotificationVisibility(
                    DownloadManager.Request.VISIBILITY_VISIBLE
                )
                .setTitle("Wallies")
                .setDestinationUri(Uri.fromFile(file))
        }
        downloadManager.enqueue(request)
        return true
    } catch (e: Exception) {
        Toast.makeText(this, "Image download failed: ${e.message}", Toast.LENGTH_SHORT)
            .show()
    }
    return false
}

fun Uri.toBitmap(context: Context): Bitmap? {
    val contentResolver: ContentResolver = context.contentResolver

    var bitmap: Bitmap? = null
    if (Build.VERSION.SDK_INT >= 29) {
        val source: ImageDecoder.Source = ImageDecoder.createSource(contentResolver, this)
        try {
            bitmap = ImageDecoder.decodeBitmap(source)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    } else {
        try {
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, this)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return bitmap
}
