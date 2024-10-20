package com.oguzdogdu.walliescompose.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.oguzdogdu.walliescompose.features.authenticateduser.navigateToAuthenticatedUserScreen
import com.oguzdogdu.walliescompose.features.collections.collectionScreen
import com.oguzdogdu.walliescompose.features.collections.detaillist.navigateToCollectionDetailListScreen
import com.oguzdogdu.walliescompose.features.detail.navigateToDetailScreen
import com.oguzdogdu.walliescompose.features.favorites.favoritesScreen
import com.oguzdogdu.walliescompose.features.home.homeScreen
import com.oguzdogdu.walliescompose.features.popular.navigateToPopularScreen
import com.oguzdogdu.walliescompose.features.search.navigateToSearchScreen
import com.oguzdogdu.walliescompose.features.settings.settingsScreen
import com.oguzdogdu.walliescompose.features.topics.navigateToTopicsScreen
import com.oguzdogdu.walliescompose.features.topics.topicdetaillist.navigateToTopicDetailListScreen
import kotlinx.serialization.Serializable

@Serializable
object NavigationBarGraph

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.navigationBarGraph(
    navHostController: NavHostController,
    scope: SharedTransitionScope
) {
    navigation<NavigationBarGraph>(startDestination = Screens.HomeScreenNavigationRoute) {
        homeScreen(
            transitionScope = scope,
            onTopicDetailListClick = {
                navHostController.navigateToTopicDetailListScreen(topicId = it)
            },
            onPopularClick = {
                navHostController.navigateToDetailScreen(photoId = it)
            },
            onTopicSeeAllClick = {
                navHostController.navigateToTopicsScreen()
            },
            onSearchClick = {
                navHostController.navigateToSearchScreen()
            },
            onPopularSeeAllClick = {
                navHostController.navigateToPopularScreen()
            },
            onUserPhotoClick = {
                navHostController.navigateToAuthenticatedUserScreen()
            },
            onRandomImageClick = {
                navHostController.navigateToDetailScreen(photoId = it)
            }
        )
        collectionScreen(
            onCollectionClick = { id, title ->
                navHostController.navigateToCollectionDetailListScreen(
                    collectionDetailListId = id,
                    collectionDetailListTitle = title
                )
            })
        favoritesScreen(onFavoriteClick = {
            navHostController.navigateToDetailScreen(photoId = it)
        })
        settingsScreen()
    }
}