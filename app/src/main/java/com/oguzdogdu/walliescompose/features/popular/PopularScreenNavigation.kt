package com.oguzdogdu.walliescompose.features.popular

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val PopularScreenNavigationRoute = "popular_screen_route"

fun NavController.navigateToPopularScreen(
    navOptions: NavOptions? = null,
) {
    this.navigate(PopularScreenNavigationRoute, navOptions)
}

fun NavGraphBuilder.popularScreen(onPopularClick: (String?) -> Unit,onBackClick: () -> Unit) {
    composable(
        PopularScreenNavigationRoute
    ) {
        PopularScreenRoute(onPopularClick = {id ->
            onPopularClick.invoke(id)
        }, onBackClick = {
            onBackClick.invoke()
        })
    }
}