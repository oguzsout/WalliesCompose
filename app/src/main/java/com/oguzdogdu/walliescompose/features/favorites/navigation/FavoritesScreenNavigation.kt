package com.oguzdogdu.walliescompose.features.favorites.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.oguzdogdu.walliescompose.features.favorites.FavoritesScreenRoute

const val FavoritesScreenNavigationRoute = "favorites_screen_route"

fun NavController.navigateToFavoritesScreen(
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