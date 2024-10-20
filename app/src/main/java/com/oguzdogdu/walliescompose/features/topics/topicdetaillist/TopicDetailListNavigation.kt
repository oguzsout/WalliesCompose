package com.oguzdogdu.walliescompose.features.topics.topicdetaillist

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.oguzdogdu.walliescompose.navigation.Screens

fun NavController.navigateToTopicDetailListScreen(
    topicId: String?,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = Screens.TopicDetailListScreenNavigationRoute(topicId)) {
        navOptions()
    }
}

fun NavGraphBuilder.topicDetailListScreen(onTopicClick: (String) -> Unit, onBackClick: () -> Unit) {
    composable<Screens.TopicDetailListScreenNavigationRoute> { backStackEntry ->
        val topicDetailListScreenNavArgs =
            backStackEntry.toRoute<Screens.TopicDetailListScreenNavigationRoute>()
        TopicDetailListRoute(topicId = topicDetailListScreenNavArgs.topicId, onTopicClick = { id ->
            onTopicClick.invoke(id)
        }, onBackClick = { onBackClick.invoke() })
    }
}