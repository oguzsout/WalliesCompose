package com.oguzdogdu.walliescompose.features.collections

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

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