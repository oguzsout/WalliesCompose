package com.oguzdogdu.walliescompose.features.authenticateduser.changeemail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val ChangeEmailScreenNavigationRoute = "change_email_screen_route"

fun NavController.navigateToChangeEmailScreen(
    navOptions: NavOptions? = null,
) {
    this.navigate(ChangeEmailScreenNavigationRoute, navOptions)
}

fun NavGraphBuilder.changeEmailScreen(
    navigateBack: () -> Unit,
) {
    composable(
        ChangeEmailScreenNavigationRoute
    ) {
        ChangeEmailScreenRoute(navigateBack = {
            navigateBack.invoke()
        })
    }
}