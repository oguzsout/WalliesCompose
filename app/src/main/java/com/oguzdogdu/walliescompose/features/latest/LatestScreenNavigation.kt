package com.oguzdogdu.walliescompose.features.latest

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val LatestScreenNavigationRoute = "latest_screen_route"

fun NavController.navigateToLatestScreen(
    navOptions: NavOptions? = null,
) {
    this.navigate(LatestScreenNavigationRoute, navOptions)
}

fun NavGraphBuilder.latestScreen(onLatestClick: (String?) -> Unit, onBackClick: () -> Unit) {
    composable(
        LatestScreenNavigationRoute
    ) {
        LatestScreenRoute(onLatestClick = { id ->
            onLatestClick.invoke(id)
        }, onBackClick = {
            onBackClick.invoke()
        })
    }
}