package com.oguzdogdu.walliescompose.features.authenticateduser.changenameandsurname

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object ChangeNameAndSurnameScreenRoute

fun NavController.navigateToChangeNameAndASurnameScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = ChangeNameAndSurnameScreenRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.changeNameAndSurnameScreen(
    navigateBack: () -> Unit,
) {
    composable<ChangeNameAndSurnameScreenRoute> {
        ChangeNameAndSurnameScreenRoute(navigateBack = {
            navigateBack.invoke()
        })
    }
}