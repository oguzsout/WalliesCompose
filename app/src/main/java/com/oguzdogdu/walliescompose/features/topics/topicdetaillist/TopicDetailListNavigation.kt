package com.oguzdogdu.walliescompose.features.topics.topicdetaillist

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val TopicDetailListScreenNavigationRoute = "topic_detail_list_screen_route"

fun NavController.navigateToTopicDetailListScreen(
    topicId: String?,
    navOptions: NavOptions? = null,
) {
    this.navigate("$TopicDetailListScreenNavigationRoute/$topicId", navOptions)
}

fun NavGraphBuilder.topicDetailListScreen(onTopicClick: (String) -> Unit, onBackClick: () -> Unit) {
    composable(
        "$TopicDetailListScreenNavigationRoute/{topicId}",
        arguments = listOf(
            navArgument("topicId") {
                type = NavType.StringType
            }
        )
    ) {
        val topicId = it.arguments?.getString("topicId")
        TopicDetailListRoute(topicId = topicId, onTopicClick = {id ->
            onTopicClick.invoke(id)
        }, onBackClick = { onBackClick.invoke() })
    }
}