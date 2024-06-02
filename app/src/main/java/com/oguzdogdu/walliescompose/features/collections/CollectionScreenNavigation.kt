package com.oguzdogdu.walliescompose.features.collections

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object CollectionScreenNavigationRoute

fun NavController.navigateToCollectionScreen(
    navOptions: NavOptions? = null,
) {
    navigate(route = CollectionScreenNavigationRoute, navOptions)
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.collectionScreen(transitionScope: SharedTransitionScope, onCollectionClick: (String, String) -> Unit) {
    composable<CollectionScreenNavigationRoute> {
        transitionScope.CollectionsScreenRoute(
            animatedVisibilityScope = this,
            onCollectionClick = {id,title ->
            onCollectionClick.invoke(id,title)
        })
    }
}