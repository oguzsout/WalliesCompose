package com.oguzdogdu.walliescompose.features.search

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.oguzdogdu.walliescompose.navigation.Screens

fun NavController.navigateToSearchScreen(
    queryFromDetail:String? = null,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = Screens.SearchScreenNavigationRoute(queryFromDetail)) {
        navOptions()
    }
}

fun NavGraphBuilder.searchScreen(
    onBackClick: () -> Unit,
    searchPhotoClick: (String) -> Unit,
    searchUserClick: (String) -> Unit
) {
    composable<Screens.SearchScreenNavigationRoute>{ backStackEntry ->
        val searchScreenArgs = backStackEntry.toRoute<Screens.SearchScreenNavigationRoute>()
        SearchScreenRoute(onBackClick = {
            onBackClick.invoke()
        }, searchPhotoClick = {id ->
            searchPhotoClick.invoke(id)
        }, searchUserClick = {id ->
            searchUserClick.invoke(id)
        },queryFromDetail = searchScreenArgs.queryFromDetail)
    }
}