package com.oguzdogdu.walliescompose.features.collections

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

fun NavGraphBuilder.collectionScreen(onCollectionClick: (String, String) -> Unit) {
    composable<CollectionScreenNavigationRoute> {
        CollectionsScreenRoute(
            onCollectionClick = {id,title ->
            onCollectionClick.invoke(id,title)
        })
    }
}