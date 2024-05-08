package com.oguzdogdu.walliescompose.features.latest

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val LatestScreenNavigationRoute = "latest_screen_route"

fun NavController.navigateToLatestScreen(
    navOptions: NavOptions? = null,
) {
    this.navigate(LatestScreenNavigationRoute, navOptions)
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.latestScreen(
    transitionScope: SharedTransitionScope,
    onLatestClick: (String?) -> Unit,
    onBackClick: () -> Unit
) {
    composable(
        LatestScreenNavigationRoute
    ) {
        transitionScope.LatestScreenRoute(
            animatedVisibilityScope = this,
            onLatestClick = { id ->
                onLatestClick.invoke(id)
            }, onBackClick = {
                onBackClick.invoke()
            })
    }
}