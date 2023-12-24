package com.oguzdogdu.walliescompose.features.collections.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.oguzdogdu.walliescompose.features.collections.CollectionsScreenRoute

const val CollectionScreenNavigationRoute = "collection_screen_route"

fun NavController.navigateToCollectionScreen(
    navOptions: NavOptions? = null,
) {
    this.navigate(CollectionScreenNavigationRoute, navOptions)
}

fun NavGraphBuilder.collectionScreen() {
    composable(
        CollectionScreenNavigationRoute
    ) {
        CollectionsScreenRoute()
    }
}