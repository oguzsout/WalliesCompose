package com.oguzdogdu.walliescompose.features.signup

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object SignUpScreenNavigationRoute

fun NavController.navigateToSignUpScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = SignUpScreenNavigationRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.signUpScreen(
    navigateBack: () -> Unit,
    goToLoginScreen: () -> Unit
) {
    composable<SignUpScreenNavigationRoute> {
        SignUpScreenRoute(onBackClick = {
            navigateBack.invoke()
        }, goToLoginScreen = {
            goToLoginScreen.invoke()
        })
    }
}