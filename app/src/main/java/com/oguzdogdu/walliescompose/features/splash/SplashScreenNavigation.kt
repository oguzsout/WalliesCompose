package com.oguzdogdu.walliescompose.features.splash

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val SplashScreenNavigationRoute = "splash_screen_route"

fun NavController.navigateToSplashScreen(
    navOptions: NavOptions? = null,
) {
    this.navigate(SplashScreenNavigationRoute, navOptions)
}

fun NavGraphBuilder.splashScreen(
    goToLoginFlow: () -> Unit,
    goToContentScreen: () -> Unit
) {
    composable(
        SplashScreenNavigationRoute,
    ) {
        SplashScreenRoute(goToLoginFlow = {
            goToLoginFlow.invoke()
        }, goToContentScreen = {
            goToContentScreen.invoke()
        })
    }
}