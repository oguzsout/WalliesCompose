package com.oguzdogdu.walliescompose.util

import android.content.Context
import android.widget.Toast
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.data.common.Constants

 fun Context.downloadImageFromWeb(imageTitle:String, url: String?, success:(Boolean) -> Unit,onDismiss:() -> Unit) {
    val directory: String = this.getString(R.string.app_name)
    val fileName = imageTitle + Constants.FILE_NAME_SUFFIX
    Toast.makeText(this, R.string.downloading_text, Toast.LENGTH_LONG).show()
    val downloadableImage = url?.let { this.downloadImage(it, directory, fileName) }
    if (downloadableImage == true) {
        Toast.makeText(this, R.string.download_photo_success, Toast.LENGTH_LONG).show()
        success.invoke(true)
        onDismiss.invoke()
    } else {
        success.invoke(false)
        onDismiss.invoke()
    }
}