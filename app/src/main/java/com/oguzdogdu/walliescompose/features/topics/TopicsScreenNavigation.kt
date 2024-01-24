package com.oguzdogdu.walliescompose.features.topics

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val TopicsScreenNavigationRoute = "topics_screen_route"

fun NavController.navigateToTopicsScreen(
    navOptions: NavOptions? = null,
) {
    this.navigate(TopicsScreenNavigationRoute, navOptions)
}

fun NavGraphBuilder.topicsScreen(onBackClick: () -> Unit) {
    composable(
        TopicsScreenNavigationRoute
    ) {
        TopicsScreenRoute(onBackClick = { onBackClick.invoke() })
    }
}