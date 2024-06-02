package com.oguzdogdu.walliescompose.features.topics.topicdetaillist

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data class TopicDetailListScreenNavigationRoute(val topicId: String? = null)

fun NavController.navigateToTopicDetailListScreen(
    topicId: String?,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = TopicDetailListScreenNavigationRoute(topicId)) {
        navOptions()
    }
}

fun NavGraphBuilder.topicDetailListScreen(onTopicClick: (String) -> Unit, onBackClick: () -> Unit) {
    composable<TopicDetailListScreenNavigationRoute> { backStackEntry ->
        val topicDetailListScreenNavArgs =
            backStackEntry.toRoute<TopicDetailListScreenNavigationRoute>()
        TopicDetailListRoute(topicId = topicDetailListScreenNavArgs.topicId, onTopicClick = { id ->
            onTopicClick.invoke(id)
        }, onBackClick = { onBackClick.invoke() })
    }
}