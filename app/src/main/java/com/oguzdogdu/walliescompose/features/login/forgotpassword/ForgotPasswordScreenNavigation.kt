package com.oguzdogdu.walliescompose.features.login.forgotpassword

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val ForgotPasswordScreenNavigationRoute = "forgot_password_screen_route"

fun NavController.navigateToForgotPasswordScreen(
    navOptions: NavOptions? = null,
) {
    this.navigate(ForgotPasswordScreenNavigationRoute, navOptions)
}

fun NavGraphBuilder.forgotPasswordScreen(
    navigateToHome: () -> Unit,
    navigateBack: () -> Unit
) {
    composable(
        ForgotPasswordScreenNavigationRoute
    ) {
        ForgotPasswordScreenRoute(
            navigateToHome = { navigateToHome.invoke() },
            navigateBack = { navigateBack.invoke() })
    }
}