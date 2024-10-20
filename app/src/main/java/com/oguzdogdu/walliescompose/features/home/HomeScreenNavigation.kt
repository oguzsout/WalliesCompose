package com.oguzdogdu.walliescompose.features.home

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.oguzdogdu.walliescompose.navigation.Screens

fun NavController.navigateToHomeScreen(
    navOptions: NavOptions? = null,
) {
    navigate(route = Screens.HomeScreenNavigationRoute,navOptions)
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.homeScreen(
    transitionScope: SharedTransitionScope,
    onTopicSeeAllClick: () -> Unit,
    onPopularSeeAllClick: () -> Unit,
    onTopicDetailListClick: (String?) -> Unit,
    onPopularClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onUserPhotoClick: () -> Unit,
    onRandomImageClick: (String?) -> Unit,
) {
    composable<Screens.HomeScreenNavigationRoute> {
        val viewModel: HomeViewModel = hiltViewModel()
        val localLifecycleOwner = LocalLifecycleOwner.current
        val homeUiState by viewModel.homeListState.collectAsStateWithLifecycle(lifecycleOwner = localLifecycleOwner)
        transitionScope.HomeScreenRoute(
            viewModel = viewModel,
            homeUiState = homeUiState,
            animatedVisibilityScope = this,
            onTopicSeeAllClick = {
            onTopicSeeAllClick.invoke()
        },
            onPopularSeeAllClick = {
                onPopularSeeAllClick.invoke()
            }, onTopicDetailListClick = {
                onTopicDetailListClick.invoke(it)
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