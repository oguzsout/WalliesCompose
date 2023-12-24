package com.oguzdogdu.walliescompose.features.appstate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.oguzdogdu.walliescompose.features.home.navigation.HomeScreenNavigationRoute
import com.oguzdogdu.walliescompose.features.collections.navigation.navigateToCollectionScreen
import com.oguzdogdu.walliescompose.features.favorites.navigation.navigateToFavoritesScreen
import com.oguzdogdu.walliescompose.features.home.navigation.navigateToHomeScreen
import com.oguzdogdu.walliescompose.features.settings.navigation.navigateTosettingsScreen
import com.oguzdogdu.walliescompose.navigation.TopLevelDestination

@Composable
fun rememberMainAppState(
    navController: NavHostController = rememberNavController()
): MainAppState {
    return remember(navController) {
        MainAppState(navController)
    }
}

@Stable
class MainAppState(
    val navController: NavHostController
) {
    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val shouldShowBottomBar: Boolean
        @Composable get() = currentDestination?.hierarchy?.any { destination ->
            topLevelDestinations.any {
                destination.route?.contains(it.route) ?: false
            }
        } ?: false

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries


    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelOptions = navOptions {
            popUpTo(HomeScreenNavigationRoute) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.WALLPAPERS -> navController.navigateToHomeScreen(topLevelOptions)
            TopLevelDestination.COLLECTIONS -> navController.navigateToCollectionScreen(
                topLevelOptions
            )

            TopLevelDestination.FAVORITES -> navController.navigateToFavoritesScreen(topLevelOptions)
            TopLevelDestination.SETTINGS -> navController.navigateTosettingsScreen(topLevelOptions)
        }
    }

    fun onBackPress() {
        navController.popBackStack()
    }

}