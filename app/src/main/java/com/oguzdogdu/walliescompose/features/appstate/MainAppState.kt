package com.oguzdogdu.walliescompose.features.appstate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.oguzdogdu.walliescompose.features.collections.navigateToCollectionScreen
import com.oguzdogdu.walliescompose.features.favorites.navigateToFavoritesScreen
import com.oguzdogdu.walliescompose.features.home.navigateToHomeScreen
import com.oguzdogdu.walliescompose.features.settings.navigateToSettingsScreen
import com.oguzdogdu.walliescompose.navigation.TopLevelDestination

@Composable
fun rememberMainAppState(
    navController: NavHostController = rememberNavController(),
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
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val shouldShowBottomBar: Boolean
        @Composable get() = currentDestination?.hierarchy?.any { destination ->
            topLevelDestinations.any {
                destination.route?.contains(it.route) ?: false
            }
        } ?: false

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries


    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.WALLPAPERS -> navController.navigateToHomeScreen(topLevelNavOptions)
            TopLevelDestination.COLLECTIONS -> navController.navigateToCollectionScreen(topLevelNavOptions)
            TopLevelDestination.FAVORITES -> navController.navigateToFavoritesScreen(topLevelNavOptions)
            TopLevelDestination.SETTINGS -> navController.navigateToSettingsScreen(topLevelNavOptions)
        }
    }

    fun onBackPress() {
        navController.popBackStack()
    }
}