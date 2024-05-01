package com.oguzdogdu.walliescompose.features.popular

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.oguzdogdu.walliescompose.domain.model.popular.PopularImage

const val PopularScreenNavigationRoute = "popular_screen_route"

fun NavController.navigateToPopularScreen(
    navOptions: NavOptions? = null,
) {
    this.navigate(PopularScreenNavigationRoute, navOptions)
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.popularScreen(
    transitionScope: SharedTransitionScope,
    onPopularClick: (String?) -> Unit, onBackClick: () -> Unit
) {
    composable(
        PopularScreenNavigationRoute
    ) {
        transitionScope.PopularScreenRoute(
            animatedVisibilityScope = this,
            onPopularClick = {id ->
            onPopularClick.invoke(id)
        }, onBackClick = {
            onBackClick.invoke()
        })
    }
}