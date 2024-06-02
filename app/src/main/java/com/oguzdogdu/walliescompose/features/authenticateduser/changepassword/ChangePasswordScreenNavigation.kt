package com.oguzdogdu.walliescompose.features.authenticateduser.changepassword

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object ChangePasswordScreenRoute

fun NavController.navigateToChangePasswordScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = ChangePasswordScreenRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.changePasswordScreen(
    navigateBack: () -> Unit,
) {
    composable<ChangePasswordScreenRoute> {
        ChangePasswordScreenRoute(navigateBack = {
            navigateBack.invoke()
        })
    }
}