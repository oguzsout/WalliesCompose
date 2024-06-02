package com.oguzdogdu.walliescompose.features.latest

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object LatestScreenNavigationRoute

fun NavController.navigateToLatestScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = LatestScreenNavigationRoute) {
        navOptions()
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.latestScreen(
    transitionScope: SharedTransitionScope,
    onLatestClick: (String?) -> Unit,
    onBackClick: () -> Unit
) {
    composable<LatestScreenNavigationRoute> {
        transitionScope.LatestScreenRoute(
            animatedVisibilityScope = this,
            onLatestClick = { id ->
                onLatestClick.invoke(id)
            }, onBackClick = {
                onBackClick.invoke()
            })
    }
}