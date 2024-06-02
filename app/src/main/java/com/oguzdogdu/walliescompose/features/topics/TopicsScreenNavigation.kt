package com.oguzdogdu.walliescompose.features.topics

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object TopicsScreenNavigationRoute

fun NavController.navigateToTopicsScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = TopicsScreenNavigationRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.topicsScreen(onBackClick: () -> Unit,onTopicClick: (String) -> Unit) {
    composable<TopicsScreenNavigationRoute >{
        TopicsScreenRoute(onBackClick = { onBackClick.invoke() }, onTopicClick = {
            onTopicClick.invoke(it)
        })
    }
}