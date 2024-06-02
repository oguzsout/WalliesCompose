package com.oguzdogdu.walliescompose.features.favorites

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object FavoritesScreenNavigationRoute

fun NavController.navigateToFavoritesScreen(
    navOptions: NavOptions? = null,
) {
    navigate(route = FavoritesScreenNavigationRoute, navOptions)
}

fun NavGraphBuilder.favoritesScreen(onFavoriteClick: (String?) -> Unit) {
    composable<FavoritesScreenNavigationRoute> {
        FavoritesScreenRoute(onFavoriteClick = {
            onFavoriteClick.invoke(it)
        })
    }
}