package com.oguzdogdu.walliescompose.features.authenticateduser

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val AuthenticatedUserScreenNavigationRoute = "authenticated_user_screen_route"

fun NavController.navigateToAuthenticatedUserScreen(
    navOptions: NavOptions? = null,
) {
    this.navigate(AuthenticatedUserScreenNavigationRoute, navOptions)
}

fun NavGraphBuilder.authenticatedUserScreen(
    navigateBack: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToChangeNameAndSurname: () -> Unit,
    navigateToChangePassword: () -> Unit,
    navigateToChangeEmail: () -> Unit,

    ) {
    composable(
        AuthenticatedUserScreenNavigationRoute
    ) {
        AuthenticatedUserScreenRoute(
           navigateBack = {
               navigateBack.invoke()
           },
            navigateToLogin = {
                navigateToLogin.invoke()
            },
            navigateToChangeNameAndSurname = {
                navigateToChangeNameAndSurname.invoke()
            },
            navigateToChangePassword = {
                navigateToChangePassword.invoke()
            }, navigateToChangeEmail = {
                navigateToChangeEmail.invoke()
            }
        )
    }
}