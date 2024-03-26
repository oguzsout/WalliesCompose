package com.oguzdogdu.walliescompose.features.authenticateduser.changenameandsurname

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val ChangeNameAndSurnameScreenNavigationRoute = "change_name_and_surname_screen_route"

fun NavController.navigateToChangeNameAndASurnameScreen(
    navOptions: NavOptions? = null,
) {
    this.navigate(ChangeNameAndSurnameScreenNavigationRoute, navOptions)
}

fun NavGraphBuilder.changeNameAndSurnameScreen(
    navigateBack: () -> Unit,
) {
    composable(
        ChangeNameAndSurnameScreenNavigationRoute
    ) {
        ChangeNameAndSurnameScreenRoute(navigateBack = {
            navigateBack.invoke()
        })
    }
}