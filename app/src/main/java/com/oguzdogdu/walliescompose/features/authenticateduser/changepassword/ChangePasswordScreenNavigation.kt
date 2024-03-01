package com.oguzdogdu.walliescompose.features.authenticateduser.changepassword

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.oguzdogdu.walliescompose.features.authenticateduser.AuthenticatedUserScreenRoute

const val ChangePasswordScreenNavigationRoute = "change_password_screen_route"

fun NavController.navigateToChangePasswordScreen(
    navOptions: NavOptions? = null,
) {
    this.navigate(ChangePasswordScreenNavigationRoute, navOptions)
}

fun NavGraphBuilder.changePasswordScreen(
    navigateBack: () -> Unit,
) {
    composable(
        ChangePasswordScreenNavigationRoute
    ) {
        ChangePasswordScreenRoute(navigateBack = {
            navigateBack.invoke()
        })
    }
}