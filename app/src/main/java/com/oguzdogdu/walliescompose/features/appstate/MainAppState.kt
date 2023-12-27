package com.oguzdogdu.walliescompose.features.appstate

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.oguzdogdu.walliescompose.features.home.HomeScreenNavigationRoute
import com.oguzdogdu.walliescompose.features.collections.navigateToCollectionScreen
import com.oguzdogdu.walliescompose.features.favorites.navigateTofavoritesScreen
import com.oguzdogdu.walliescompose.features.home.navigateToHomeScreen
import com.oguzdogdu.walliescompose.features.settings.navigateTosettingsScreen
import com.oguzdogdu.walliescompose.navigation.TopLevelDestination
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberMainAppState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    windowSizeClass: WindowSizeClass
): MainAppState {
    return remember(navController, coroutineScope) {
        MainAppState(navController, coroutineScope,windowSizeClass)
    }
}

@Stable
class MainAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val windowSizeClass: WindowSizeClass
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val shouldShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

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
            TopLevelDestination.COLLECTIONS -> navController.navigateToCollectionScreen(topLevelOptions)
            TopLevelDestination.FAVORITES -> navController.navigateTofavoritesScreen(topLevelOptions)
            TopLevelDestination.SETTINGS -> navController.navigateTosettingsScreen(topLevelOptions)
        }
    }

    fun onBackPress() {
        navController.popBackStack()
    }

}