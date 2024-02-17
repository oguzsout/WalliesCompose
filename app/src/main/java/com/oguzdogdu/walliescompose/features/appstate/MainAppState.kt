package com.oguzdogdu.walliescompose.features.appstate

import android.app.Activity
import android.content.Context
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
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
import com.oguzdogdu.walliescompose.features.login.googlesignin.GoogleAuthUiClient
import com.oguzdogdu.walliescompose.features.settings.navigateToSettingsScreen
import com.oguzdogdu.walliescompose.navigation.TopLevelDestination
import com.oguzdogdu.walliescompose.util.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberMainAppState(
    navController: NavHostController = rememberNavController(),
     coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor,
): MainAppState {
    return remember(navController) {
        MainAppState(navController,coroutineScope,networkMonitor)
    }
}

@Stable
class MainAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor,
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

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

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
            TopLevelDestination.COLLECTIONS -> navController.navigateToCollectionScreen(
                topLevelNavOptions
            )

            TopLevelDestination.FAVORITES -> navController.navigateToFavoritesScreen(
                topLevelNavOptions
            )

            TopLevelDestination.SETTINGS -> navController.navigateToSettingsScreen(
                topLevelNavOptions
            )
        }
    }

    fun onBackPress() {
        navController.popBackStack()
    }
    fun canNavigateBack(): Boolean {
        return navController.previousBackStackEntry != null
    }
}