package com.oguzdogdu.walliescompose.features.home

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val HomeScreenNavigationRoute = "home_screen_route"

fun NavController.navigateToHomeScreen(
    navOptions: NavOptions? = null,
) {
    this.navigate(HomeScreenNavigationRoute, navOptions)
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.homeScreen(
    transitionScope: SharedTransitionScope,
    onTopicSeeAllClick: () -> Unit,
    onPopularSeeAllClick: () -> Unit,
    onLatestSeeAllClick: () -> Unit,
    onTopicDetailListClick: (String?) -> Unit,
    onPopularClick: (String) -> Unit,
    onLatestClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onUserPhotoClick: () -> Unit,
    navigateBack:() -> Unit
) {
    composable(
        HomeScreenNavigationRoute
    ) {
        val viewModel : HomeViewModel = hiltViewModel()
        transitionScope.HomeScreenRoute(
            animatedVisibilityScope = this,
            onTopicSeeAllClick = {
            onTopicSeeAllClick.invoke()
        },
            viewModel = viewModel,
            onTopicDetailListClick = {
                onTopicDetailListClick.invoke(it)
            }, onPopularClick = {
                onPopularClick.invoke(it)
            }, onLatestClick = {
                onLatestClick.invoke(it)
            }, onSearchClick = { onSearchClick.invoke() },
            onPopularSeeAllClick = {
                onPopularSeeAllClick.invoke()
            },
            onLatestSeeAllClick = {
                onLatestSeeAllClick.invoke()
            }, navigateBack = {
                navigateBack.invoke()
            },
            onUserPhotoClick = {
                onUserPhotoClick.invoke()
            }
        )
    }
}