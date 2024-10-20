package com.oguzdogdu.walliescompose.features.collections

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.oguzdogdu.walliescompose.navigation.Screens

fun NavController.navigateToCollectionScreen(
    navOptions: NavOptions? = null,
) {
    navigate(route = Screens.CollectionScreenNavigationRoute, navOptions)
}

fun NavGraphBuilder.collectionScreen(onCollectionClick: (String, String) -> Unit) {
    composable<Screens.CollectionScreenNavigationRoute> {
        CollectionsScreenRoute(
            onCollectionClick = {id,title ->
            onCollectionClick.invoke(id,title)
        })
    }
}