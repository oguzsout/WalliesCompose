package com.oguzdogdu.walliescompose.features.login.forgotpassword

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.oguzdogdu.walliescompose.navigation.Screens

fun NavController.navigateToForgotPasswordScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = Screens.ForgotPasswordScreenNavigationRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.forgotPasswordScreen(
    navigateToHome: () -> Unit,
    navigateBack: () -> Unit
) {
    composable<Screens.ForgotPasswordScreenNavigationRoute> {
        ForgotPasswordScreenRoute(
            navigateToHome = { navigateToHome.invoke() },
            navigateBack = { navigateBack.invoke() })
    }
}