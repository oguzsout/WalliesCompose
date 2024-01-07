package com.oguzdogdu.walliescompose

import android.app.Application
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import com.oguzdogdu.walliescompose.features.settings.ThemeValues
import dagger.hilt.android.HiltAndroidApp
@Stable
@HiltAndroidApp
class WalliesApplication : Application() {
    val theme = mutableStateOf("")
}