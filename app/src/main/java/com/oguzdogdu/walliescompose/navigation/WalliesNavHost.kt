package com.oguzdogdu.walliescompose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.oguzdogdu.walliescompose.features.home.HomeScreenNavigationRoute
import com.oguzdogdu.walliescompose.features.collections.collectionScreen
import com.oguzdogdu.walliescompose.features.favorites.favoritesScreen
import com.oguzdogdu.walliescompose.features.home.homeScreen
import com.oguzdogdu.walliescompose.features.settings.settingsScreen

@Composable
fun WalliesNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = HomeScreenNavigationRoute,
) {
    NavHost(
        modifier = modifier,
        navController = navController, startDestination = startDestination
    ) {
        homeScreen()
        collectionScreen()
        favoritesScreen()
        settingsScreen()
    }
}
