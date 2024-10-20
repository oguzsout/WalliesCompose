package com.oguzdogdu.walliescompose.features.topics

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.oguzdogdu.walliescompose.navigation.Screens

fun NavController.navigateToTopicsScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = Screens.TopicsScreenNavigationRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.topicsScreen(onBackClick: () -> Unit,onTopicClick: (String) -> Unit) {
    composable<Screens.TopicsScreenNavigationRoute>{
        TopicsScreenRoute(onBackClick = { onBackClick.invoke() }, onTopicClick = {
            onTopicClick.invoke(it)
        })
    }
}