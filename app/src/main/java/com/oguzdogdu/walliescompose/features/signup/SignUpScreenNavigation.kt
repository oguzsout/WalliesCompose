package com.oguzdogdu.walliescompose.features.signup

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.oguzdogdu.walliescompose.features.login.googlesignin.GoogleAuthUiClient

const val SignUpScreenNavigationRoute = "sign_up_screen_route"

fun NavController.navigateToSignUpScreen(
    navOptions: NavOptions? = null,
) {
    this.navigate(SignUpScreenNavigationRoute, navOptions)
}

fun NavGraphBuilder.signUpScreen(
    navigateBack: () -> Unit
) {
    composable(
        SignUpScreenNavigationRoute
    ) {
        SignUpScreenRoute(onBackClick = {
            navigateBack.invoke()
        })
    }
}