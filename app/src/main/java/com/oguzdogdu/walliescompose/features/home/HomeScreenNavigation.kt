package com.oguzdogdu.walliescompose.features.home

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object HomeScreenNavigationRoute

fun NavController.navigateToHomeScreen(
    navOptions: NavOptions? = null,
) {
    navigate(route = HomeScreenNavigationRoute,navOptions)
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
    onRandomImageClick: (String?) -> Unit,
    navigateBack:() -> Unit
) {
    composable<HomeScreenNavigationRoute> {
        transitionScope.HomeScreenRoute(
            animatedVisibilityScope = this,
            onTopicSeeAllClick = {
            onTopicSeeAllClick.invoke()
        },
            onPopularSeeAllClick = {
                onPopularSeeAllClick.invoke()
            }, onLatestSeeAllClick = {
                onLatestSeeAllClick.invoke()
            }, onTopicDetailListClick = {
                onTopicDetailListClick.invoke(it)
            }, onLatestClick = {
                onLatestClick.invoke(it)
            },
            onPopularClick = {
                onPopularClick.invoke(it)
            },
            onSearchClick = { onSearchClick.invoke() }, onUserPhotoClick = {
                onUserPhotoClick.invoke()
            },
            onRandomImageClick = {
                onRandomImageClick.invoke(it)
            }
        )
    }
}