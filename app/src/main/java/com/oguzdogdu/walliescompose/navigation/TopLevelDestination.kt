package com.oguzdogdu.walliescompose.navigation

import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.features.collections.CollectionScreenNavigationRoute
import com.oguzdogdu.walliescompose.features.favorites.FavoritesScreenNavigationRoute
import com.oguzdogdu.walliescompose.features.home.HomeScreenNavigationRoute
import com.oguzdogdu.walliescompose.features.settings.SettingsScreenNavigationRoute
import kotlin.reflect.KClass

enum class TopLevelDestination(
    val icon: Int,
    val iconTextId: Int,
    val route: KClass<*>,
) {
    WALLPAPERS( 
        icon = R.drawable.wallpaper,
        iconTextId = R.string.wallpapers_title,
        route = HomeScreenNavigationRoute::class
    ),
    COLLECTIONS(
        icon = R.drawable.collections,
        iconTextId = R.string.collections_title,
        route = CollectionScreenNavigationRoute::class
    ),
    FAVORITES(
        icon = R.drawable.favorite,
        iconTextId = R.string.favorites_title,
        route = FavoritesScreenNavigationRoute::class
    ),
    SETTINGS(
        icon = R.drawable.settings,
        iconTextId = R.string.settings_title,
        route = SettingsScreenNavigationRoute::class
    ),
}
