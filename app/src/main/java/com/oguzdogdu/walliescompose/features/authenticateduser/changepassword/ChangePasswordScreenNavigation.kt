package com.oguzdogdu.walliescompose.features.authenticateduser.changepassword

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.oguzdogdu.walliescompose.navigation.Screens

fun NavController.navigateToChangePasswordScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = Screens.ChangePasswordScreenRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.changePasswordScreen(
    navigateBack: () -> Unit,
) {
    composable<Screens.ChangePasswordScreenRoute> {
        ChangePasswordScreenRoute(navigateBack = {
            navigateBack.invoke()
        })
    }
}