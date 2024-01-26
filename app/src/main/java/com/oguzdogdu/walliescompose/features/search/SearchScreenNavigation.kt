package com.oguzdogdu.walliescompose.features.search

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val SearchScreenNavigationRoute = "search_screen_route"

fun NavController.navigateToSearchScreen(
    navOptions: NavOptions? = null,
) {
    this.navigate(SearchScreenNavigationRoute, navOptions)
}

fun NavGraphBuilder.searchScreen(onBackClick: () -> Unit,searchPhotoClick: (String) -> Unit) {
    composable(
        SearchScreenNavigationRoute
    ) {
        SearchScreenRoute(onBackClick = {
            onBackClick.invoke()
        }, searchPhotoClick = {
            searchPhotoClick.invoke(it)
        })
    }
}