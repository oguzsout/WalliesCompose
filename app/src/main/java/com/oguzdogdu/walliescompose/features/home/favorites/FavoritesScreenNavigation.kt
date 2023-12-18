package com.oguzdogdu.walliescompose.features.home.favorites

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.oguzdogdu.walliescompose.features.home.HomeScreenRoute

const val FavoritesScreenNavigationRoute = "favorites_screen_route"

fun NavController.navigateTofavoritesScreen(
    navOptions: NavOptions? = null,
) {
    this.navigate(FavoritesScreenNavigationRoute, navOptions)
}

fun NavGraphBuilder.favoritesScreen() {
    composable(
        FavoritesScreenNavigationRoute
    ) {
        FavoritesScreenRoute()
    }
}