package com.oguzdogdu.walliescompose.features.authenticateduser.changeemail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object ChangeEmailScreenRoute

fun NavController.navigateToChangeEmailScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = ChangeEmailScreenRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.changeEmailScreen(
    navigateBack: () -> Unit,
) {
    composable<ChangeEmailScreenRoute> {
        ChangeEmailScreenRoute(navigateBack = {
            navigateBack.invoke()
        })
    }
}