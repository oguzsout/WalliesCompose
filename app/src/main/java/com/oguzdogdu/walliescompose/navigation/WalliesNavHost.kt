package com.oguzdogdu.walliescompose.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.oguzdogdu.walliescompose.features.login.googlesignin.GoogleAuthUiClient
import com.oguzdogdu.walliescompose.features.splash.splashScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun WalliesNavHost(
    navController: NavHostController,
    googleAuthUiClient: GoogleAuthUiClient
) {
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = Screens.SplashScreenRoute,
        ) {
            splashScreen(goToLoginFlow = {
                navController.navigate(AuthGraph) {
                    popUpTo(Screens.SplashScreenRoute) {
                        inclusive = true
                    }
                }
            }, goToContentScreen = {
                navController.navigate(NavigationBarGraph) {
                    popUpTo(Screens.SplashScreenRoute) {
                        inclusive = true
                    }
                }
            })
            navigationBarGraph(
                navHostController = navController,
                scope = this@SharedTransitionLayout
            )
            navigationAuthGraph(
                navHostController = navController,
                googleAuthUiClient = googleAuthUiClient
            )
            navigationHomeGraph(
                navHostController = navController,
                sharedTransitionScope = this@SharedTransitionLayout
            )
        }
    }
}