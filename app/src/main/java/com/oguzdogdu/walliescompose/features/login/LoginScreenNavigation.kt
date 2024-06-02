package com.oguzdogdu.walliescompose.features.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.oguzdogdu.walliescompose.features.login.googlesignin.GoogleAuthUiClient
import kotlinx.serialization.Serializable

@Serializable
data object LoginScreenNavigationRoute

fun NavController.navigateToLoginScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = LoginScreenNavigationRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.loginScreen(
    googleAuthUiClient: GoogleAuthUiClient,
    navigateToHome: () -> Unit,
    navigateToSignInEmail: () -> Unit,
    onContinueWithoutLoginClick: () -> Unit,
    navigateBack: () -> Unit
) {
    composable<LoginScreenNavigationRoute> {
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