package com.oguzdogdu.walliescompose.features.authenticateduser.changeemail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.oguzdogdu.walliescompose.navigation.Screens

fun NavController.navigateToChangeEmailScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = Screens.ChangeEmailScreenRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.changeEmailScreen(
    navigateBack: () -> Unit,
) {
    composable<Screens.ChangeEmailScreenRoute> {
        ChangeEmailScreenRoute(navigateBack = {
            navigateBack.invoke()
        })
    }
}