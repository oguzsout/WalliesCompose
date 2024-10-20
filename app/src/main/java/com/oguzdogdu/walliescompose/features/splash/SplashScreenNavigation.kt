package com.oguzdogdu.walliescompose.features.splash

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.oguzdogdu.walliescompose.navigation.Screens


fun NavController.navigateToSplashScreen() = navigate(Screens.SplashScreenRoute)

fun NavGraphBuilder.splashScreen(
    goToLoginFlow: () -> Unit,
    goToContentScreen: () -> Unit
) {
    composable<Screens.SplashScreenRoute> {
        SplashScreenRoute(goToLoginFlow = {
            goToLoginFlow.invoke()
        }, goToContentScreen = {
            goToContentScreen.invoke()
        })
    }
}