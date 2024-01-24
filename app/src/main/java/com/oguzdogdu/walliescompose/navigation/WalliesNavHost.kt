package com.oguzdogdu.walliescompose.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.oguzdogdu.walliescompose.features.collections.collectionScreen
import com.oguzdogdu.walliescompose.features.detail.detailScreen
import com.oguzdogdu.walliescompose.features.detail.navigateToDetailScreen
import com.oguzdogdu.walliescompose.features.favorites.favoritesScreen
import com.oguzdogdu.walliescompose.features.home.HomeScreenNavigationRoute
import com.oguzdogdu.walliescompose.features.home.homeScreen
import com.oguzdogdu.walliescompose.features.home.navigateToHomeScreen
import com.oguzdogdu.walliescompose.features.search.navigateToSearchScreen
import com.oguzdogdu.walliescompose.features.search.searchScreen
import com.oguzdogdu.walliescompose.features.settings.settingsScreen
import com.oguzdogdu.walliescompose.features.topics.navigateToTopicsScreen
import com.oguzdogdu.walliescompose.features.topics.topicsScreen

@Composable
fun WalliesNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = HomeScreenNavigationRoute,
) {
    NavHost(
        modifier = modifier,
        navController = navController, startDestination = startDestination,
        enterTransition = {
            EnterTransition.None
        }, exitTransition = {
            ExitTransition.None
        }, popEnterTransition = {
            EnterTransition.None
        }, popExitTransition = {
            ExitTransition.None
        }
    ) {
        homeScreen(
            onLatestClick = {
                navController.navigateToDetailScreen(photoId = it)
            },
            onPopularClick = {
                navController.navigateToDetailScreen(photoId = it)
            },
            onTopicSeeAllClick = {
                navController.navigateToTopicsScreen()
            },
            onSearchClick = {
                navController.navigateToSearchScreen()
            }
        )
        collectionScreen()
        favoritesScreen()
        settingsScreen()
        searchScreen(onBackClick = {
            navController.navigateToHomeScreen()
        })
        detailScreen(
            onBackClick = {
                navController.navigateToHomeScreen()
            }
        )
        topicsScreen(onBackClick = {
            navController.navigateToHomeScreen()
        })
    }
}
