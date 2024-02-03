package com.oguzdogdu.walliescompose.features.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oguzdogdu.walliescompose.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = hiltViewModel(),
    goToLoginFlow: () -> Unit,
    goToContentScreen: () -> Unit
) {
    val state by viewModel.splashState.collectAsStateWithLifecycle()

    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        viewModel.handleUIEvent(SplashScreenEvent.CheckAuthState)
    }

    LaunchedEffect(state) {
        when(state) {
            SplashScreenState.StartFlow -> {
            }
            SplashScreenState.UserNotSigned -> {
                goToLoginFlow.invoke()
            }
            SplashScreenState.UserSignedIn -> {
                goToContentScreen.invoke()
            }
        }
    }

    SplashScreenContent(modifier = modifier)
}

@Composable
fun SplashScreenContent(modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = R.drawable.logo), contentDescription = stringResource(
                id = R.string.app_logo
            )
        )
    }
}