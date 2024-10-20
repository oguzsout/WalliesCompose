package com.oguzdogdu.walliescompose.features.favorites

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.oguzdogdu.walliescompose.navigation.Screens

fun NavController.navigateToFavoritesScreen(
    navOptions: NavOptions? = null,
) {
    navigate(route = Screens.FavoritesScreenNavigationRoute, navOptions)
}

fun NavGraphBuilder.favoritesScreen(onFavoriteClick: (String?) -> Unit) {
    composable<Screens.FavoritesScreenNavigationRoute> {
        FavoritesScreenRoute(onFavoriteClick = {
            onFavoriteClick.invoke(it)
        })
    }
}