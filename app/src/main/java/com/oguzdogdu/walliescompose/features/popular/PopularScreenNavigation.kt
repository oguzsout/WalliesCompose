package com.oguzdogdu.walliescompose.features.popular

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.oguzdogdu.walliescompose.navigation.Screens

fun NavController.navigateToPopularScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = Screens.PopularScreenNavigationRoute) {
        navOptions()
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.popularScreen(
    transitionScope: SharedTransitionScope,
    onPopularClick: (String?) -> Unit, onBackClick: () -> Unit
) {
    composable<Screens.PopularScreenNavigationRoute> {
        transitionScope.PopularScreenRoute(
            animatedVisibilityScope = this,
            onPopularClick = {id ->
            onPopularClick.invoke(id)
        }, onBackClick = {
            onBackClick.invoke()
        })
    }
}