package com.oguzdogdu.walliescompose.features.main

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.auth.api.identity.Identity
import com.oguzdogdu.walliescompose.WalliesApplication
import com.oguzdogdu.walliescompose.features.appstate.WalliesApp
import com.oguzdogdu.walliescompose.features.login.googlesignin.GoogleAuthUiClient
import com.oguzdogdu.walliescompose.ui.theme.WalliesComposeTheme
import com.oguzdogdu.walliescompose.util.LocaleHelper
import com.oguzdogdu.walliescompose.util.NetworkMonitor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var application: WalliesApplication

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    private val viewModel: MainViewModel by viewModels()

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mainState by viewModel.appPreferencesState.collectAsStateWithLifecycle()
            LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
                viewModel.handleScreenEvents(MainScreenEvent.CheckUserAuthState)
                viewModel.handleScreenEvents(MainScreenEvent.ThemeChanged)
                viewModel.handleScreenEvents(MainScreenEvent.LanguageChanged)

            }

            LaunchedEffect(application.theme.value) {
                viewModel.handleScreenEvents(MainScreenEvent.ThemeChanged)
            }
            val localApplication = staticCompositionLocalOf {
                WalliesApplication()
            }
            LaunchedEffect(application.language.value) {
                viewModel.handleScreenEvents(MainScreenEvent.LanguageChanged)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    LocaleHelper(context = this@MainActivity).updateResourcesLegacy(application.language.value)
                } else {
                    LocaleHelper(context = this@MainActivity).updateResources(application.language.value)
                }
            }
            CompositionLocalProvider(localApplication provides application) {
                WalliesComposeTheme(
                    appTheme = application.theme.value
                ) {
                    WalliesApp(
                        isAuthenticated = mainState.userAuth,
                        networkMonitor = networkMonitor,
                        googleAuthUiClient = googleAuthUiClient
                    )
                }
            }
        }
    }
}
