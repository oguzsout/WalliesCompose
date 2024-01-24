package com.oguzdogdu.walliescompose.navigation

import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.features.collections.CollectionScreenNavigationRoute
import com.oguzdogdu.walliescompose.features.favorites.FavoritesScreenNavigationRoute
import com.oguzdogdu.walliescompose.features.home.HomeScreenNavigationRoute
import com.oguzdogdu.walliescompose.features.settings.SettingsScreenNavigationRoute

enum class TopLevelDestination(
    val icon: Int,
    val iconTextId: Int,
    val route: String,
) {
    WALLPAPERS( 
        icon = R.drawable.wallpaper,
        iconTextId = R.string.wallpapers_title,
        route = HomeScreenNavigationRoute
    ),
    COLLECTIONS(
        icon = R.drawable.collections,
        iconTextId = R.string.collections_title,
        route = CollectionScreenNavigationRoute
    ),
    FAVORITES(
        icon = R.drawable.favorite,
        iconTextId = R.string.favorites_title,
        route = FavoritesScreenNavigationRoute
    ),
    SETTINGS(
        icon = R.drawable.settings,
        iconTextId = R.string.settings_title,
        route = SettingsScreenNavigationRoute
    ),
}
