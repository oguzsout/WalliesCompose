package com.oguzdogdu.walliescompose.features.authenticateduser

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.oguzdogdu.walliescompose.navigation.Screens

fun NavController.navigateToAuthenticatedUserScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = Screens.AuthenticatedUserScreenRoute) {
        navOptions()
    }
 }

fun NavGraphBuilder.authenticatedUserScreen(
    navigateBack: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToChangeNameAndSurname: () -> Unit,
    navigateToChangePassword: () -> Unit,
    navigateToChangeEmail: () -> Unit,

    ) {
    composable<Screens.AuthenticatedUserScreenRoute> {
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
            },
            navigateToChangeEmail = {
                navigateToChangeEmail.invoke()
            }
        )
    }
}