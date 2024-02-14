package com.oguzdogdu.walliescompose.features.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
        Icon(
            painter = painterResource(id = R.drawable.logo), contentDescription = stringResource(
                id = R.string.app_logo
            ), tint = Color.Unspecified, modifier = modifier.clip(RoundedCornerShape(64.dp))
        )
    }
}