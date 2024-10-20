package com.oguzdogdu.walliescompose.features.signup

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.oguzdogdu.walliescompose.navigation.Screens

fun NavController.navigateToSignUpScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = Screens.SignUpScreenNavigationRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.signUpScreen(
    navigateBack: () -> Unit,
    goToLoginScreen: () -> Unit
) {
    composable<Screens.SignUpScreenNavigationRoute> {
        SignUpScreenRoute(onBackClick = {
            navigateBack.invoke()
        }, goToLoginScreen = {
            goToLoginScreen.invoke()
        })
    }
}