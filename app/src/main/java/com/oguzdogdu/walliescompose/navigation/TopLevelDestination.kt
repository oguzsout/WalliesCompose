package com.oguzdogdu.walliescompose.navigation

import com.oguzdogdu.walliescompose.R
import kotlinx.serialization.Serializable

@Serializable
enum class TopLevelDestination(
    val route: Screens,
    val icon: Int,
    val iconTextId: Int,
) {
    HomeScreenNavigationRoute(
        route = Screens.HomeScreenNavigationRoute,
        icon = R.drawable.wallpaper,
        iconTextId = R.string.wallpapers_title,
    ),
    CollectionScreenNavigationRoute(
        route = Screens.CollectionScreenNavigationRoute,
        icon = R.drawable.collections,
        iconTextId = R.string.collections_title,
    ),
    FavoritesScreenNavigationRoute(
        route = Screens.FavoritesScreenNavigationRoute,
        icon = R.drawable.favorite,
        iconTextId = R.string.favorites_title,
    ),
    SettingsScreenNavigationRoute(
        route = Screens.SettingsScreenNavigationRoute,
        icon = R.drawable.settings,
        iconTextId = R.string.settings_title,
    ),
}
