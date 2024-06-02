package com.oguzdogdu.walliescompose.features.splash

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object SplashScreenRoute

fun NavController.navigateToSplashScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(SplashScreenRoute,navOptions)

fun NavGraphBuilder.splashScreen(
    goToLoginFlow: () -> Unit,
    goToContentScreen: () -> Unit
) {
    composable<SplashScreenRoute> {
        SplashScreenRoute(goToLoginFlow = {
            goToLoginFlow.invoke()
        }, goToContentScreen = {
            goToContentScreen.invoke()
        })
    }
}