package com.oguzdogdu.walliescompose.features.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oguzdogdu.walliescompose.features.appstate.WalliesApp
import com.oguzdogdu.walliescompose.features.settings.SettingsScreenEvent
import com.oguzdogdu.walliescompose.features.settings.SettingsViewModel
import com.oguzdogdu.walliescompose.ui.theme.WalliesComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: SettingsViewModel by viewModels()
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mainUiState by viewModel.settingsState.collectAsState()
            LaunchedEffect(key1 = mainUiState.getThemeValue) {
                viewModel.handleScreenEvents(SettingsScreenEvent.ThemeChanged)
            }
            var themeState by remember {
                mutableStateOf("")
            }

            LaunchedEffect(key1 = mainUiState.getThemeValue) {
                themeState = mainUiState.getThemeValue.orEmpty()
            }

            WalliesComposeTheme(themeValues = themeState) {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    WalliesApp(windowSizeClass = calculateWindowSizeClass(this))
                }
            }
        }
    }
}
