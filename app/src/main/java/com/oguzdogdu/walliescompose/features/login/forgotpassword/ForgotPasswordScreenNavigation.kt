package com.oguzdogdu.walliescompose.features.login.forgotpassword

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object ForgotPasswordScreenNavigationRoute

fun NavController.navigateToForgotPasswordScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = ForgotPasswordScreenNavigationRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.forgotPasswordScreen(
    navigateToHome: () -> Unit,
    navigateBack: () -> Unit
) {
    composable<ForgotPasswordScreenNavigationRoute> {
        ForgotPasswordScreenRoute(
            navigateToHome = { navigateToHome.invoke() },
            navigateBack = { navigateBack.invoke() })
    }
}