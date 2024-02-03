package com.oguzdogdu.walliescompose.features.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val LoginScreenNavigationRoute = "login_screen_route"

fun NavController.navigateToLoginScreen(
    navOptions: NavOptions? = null,
) {
    this.navigate(LoginScreenNavigationRoute, navOptions)
}

fun NavGraphBuilder.loginScreen(navigateToHome:() -> Unit,onContinueWithoutLoginClick: () -> Unit) {
    composable(
        LoginScreenNavigationRoute
    ) {
       LoginScreenRoute(navigateToHome = {
           navigateToHome.invoke()
       },
           onContinueWithoutLoginClick = {
               onContinueWithoutLoginClick.invoke()
           })
    }
}