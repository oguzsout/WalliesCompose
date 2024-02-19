package com.oguzdogdu.walliescompose.features.login.signinwithemail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val SignInWithEmailScreenNavigationRoute = "sign_in_with_email_screen_route"

fun NavController.navigateToSignInWithEmailScreen(
    navOptions: NavOptions? = null,
) {
    this.navigate(SignInWithEmailScreenNavigationRoute, navOptions)
}

fun NavGraphBuilder.signInWithEmailScreen(
    navigateToHome: () -> Unit,
    navigateBack: () -> Unit,
    navigateToForgotPassword: () -> Unit
) {
    composable(
        SignInWithEmailScreenNavigationRoute
    ) {
        SignInWithEmailScreenRoute(
            navigateToHome = { navigateToHome.invoke() },
            onCreateNewAccountClick = { },
            navigateBack = { navigateBack.invoke() },
            navigateToForgotPassword = { navigateToForgotPassword.invoke() }
        )
    }
}