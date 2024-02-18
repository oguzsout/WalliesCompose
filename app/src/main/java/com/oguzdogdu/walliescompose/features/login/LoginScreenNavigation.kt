package com.oguzdogdu.walliescompose.features.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.oguzdogdu.walliescompose.features.login.googlesignin.GoogleAuthUiClient

const val LoginScreenNavigationRoute = "login_screen_route"

fun NavController.navigateToLoginScreen(
    navOptions: NavOptions? = null,
) {
    this.navigate(LoginScreenNavigationRoute, navOptions)
}

fun NavGraphBuilder.loginScreen(
    googleAuthUiClient: GoogleAuthUiClient,
    navigateToHome: () -> Unit,
    navigateToSignInEmail: () -> Unit,
    onContinueWithoutLoginClick: () -> Unit,
    navigateBack: () -> Unit
) {
    composable(
        LoginScreenNavigationRoute
    ) {
       LoginScreenRoute(googleAuthUiClient = googleAuthUiClient,navigateToHome = {
           navigateToHome.invoke()
       },
           onContinueWithoutLoginClick = {
               onContinueWithoutLoginClick.invoke()
           },
           navigateBack = {
               navigateBack.invoke()
           }, navigateToSignInEmail = {
               navigateToSignInEmail.invoke()
           })
    }
}