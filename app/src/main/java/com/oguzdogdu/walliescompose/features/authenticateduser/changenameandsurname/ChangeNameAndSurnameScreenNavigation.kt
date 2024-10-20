package com.oguzdogdu.walliescompose.features.authenticateduser.changenameandsurname

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.oguzdogdu.walliescompose.navigation.Screens

fun NavController.navigateToChangeNameAndASurnameScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = Screens.ChangeNameAndSurnameScreenRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.changeNameAndSurnameScreen(
    navigateBack: () -> Unit,
) {
    composable<Screens.ChangeNameAndSurnameScreenRoute> {
        ChangeNameAndSurnameScreenRoute(navigateBack = {
            navigateBack.invoke()
        })
    }
}