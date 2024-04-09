package com.oguzdogdu.walliescompose.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.DisplayMetrics
import android.view.WindowManager
import com.oguzdogdu.walliescompose.data.common.Constants.AUTO
import com.oguzdogdu.walliescompose.data.common.Constants.FIT
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.formatDate(
    outputFormat: String = DateFormats.DEFAULT_DATE.value,
    inputFormat: String = DateFormats.INPUT_DATE_FORMAT.value
): String {
    return try {
        val inputDateFormat = SimpleDateFormat(inputFormat, Locale.ENGLISH)
        val outputDateFormat = SimpleDateFormat(outputFormat, Locale.ENGLISH)
        outputDateFormat.format(inputDateFormat.parse(this) ?: Date())
    } catch (e: Exception) {
        e.printStackTrace()
        this
    }
}

fun String.colorize(color: Int): SpannableString {
    val spannable = SpannableString(this)
    spannable.setSpan(
        ForegroundColorSpan(color),
        0,
        this.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannable
}

fun String.toBitmap(): Bitmap {
    val imageUrl = URL(this)
    val connection = imageUrl.openConnection() as HttpURLConnection
    connection.doInput = true
    connection.connect()
    val input = connection.inputStream
    return BitmapFactory.decodeStream(input)
}

enum class DateFormats(val value: String) {
    INPUT_DATE_FORMAT("yyyy-MM-dd'T'HH:mm:ss"),
    OUTPUT_BASIC_DATE_FORMAT("MMMM yyyy"),
    OUTPUT_LAST_PAYMENT_DATE_FORMAT("dd MMMM yyyy"),
    DEFAULT_DATE("dd.MM.yyyy")
}

enum class ThemeKeys(val value: String) {
    LIGHT_THEME("1"),
    DARK_THEME("2"),
    SYSTEM_THEME("3")
}
fun String.shareExternal(): Intent {
    val dataToShare = this
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, dataToShare)
        type = "text/plain"
    }
    return Intent.createChooser(sendIntent, null)
}

 fun String.openInstagramProfile() : Intent {
    val url = "https://instagram.com/$this"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
     return Intent.createChooser(intent, null)
}

 fun String.openTwitterProfile() :Intent {
    val url = "https://twitter.com/$this"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
     return Intent.createChooser(intent, null)
}


 fun String.openPortfolioUrl() :Intent {
     val intent = Intent(Intent.ACTION_VIEW, Uri.parse(this))
     return Intent.createChooser(intent, null)
}

fun String.adjustUrlForScreenConstraints(context: Context): String {
    val displayMetrics = DisplayMetrics()
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    val width = displayMetrics.widthPixels
    val height = displayMetrics.heightPixels
    return "$this&w=$width&h=$height$FIT$AUTO"
}
