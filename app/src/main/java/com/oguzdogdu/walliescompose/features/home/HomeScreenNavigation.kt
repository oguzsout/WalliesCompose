package com.oguzdogdu.walliescompose.features.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val HomeScreenNavigationRoute = "home_screen_route"

fun NavController.navigateToHomeScreen(
    navOptions: NavOptions? = null,
) {
    this.navigate(HomeScreenNavigationRoute, navOptions)
}

fun NavGraphBuilder.homeScreen(
    onTopicClick: (String) -> Unit,
    onPopularClick: (String) -> Unit,
    onLatestClick: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    composable(
        HomeScreenNavigationRoute
    ) {
        HomeScreenRoute(onPopularClick = {
            onPopularClick.invoke(it)
        }, onLatestClick = {
            onLatestClick.invoke(it)
        }, onSearchClick = { onSearchClick.invoke() })
    }
}