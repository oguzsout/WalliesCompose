package com.oguzdogdu.walliescompose

import android.app.Application
import android.content.Context
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import com.oguzdogdu.walliescompose.features.settings.ThemeValues
import com.oguzdogdu.walliescompose.util.LocaleHelper
import dagger.hilt.android.HiltAndroidApp

@Stable
@HiltAndroidApp
class WalliesApplication : Application() {
    val theme = mutableStateOf("")
    val language = mutableStateOf("")
}