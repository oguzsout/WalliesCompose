package com.oguzdogdu.walliescompose.features.search

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.oguzdogdu.walliescompose.features.detail.DetailScreenNavigationRoute

const val SearchScreenNavigationRoute = "search_screen_route"

fun NavController.navigateToSearchScreen(
    queryFromDetail:String? = null,
    navOptions: NavOptions? = null,
) {
    this.navigate("$SearchScreenNavigationRoute/$queryFromDetail", navOptions)
}

fun NavGraphBuilder.searchScreen(
    onBackClick: () -> Unit,
    searchPhotoClick: (String) -> Unit,
    searchUserClick: (String) -> Unit
) {
    composable(
        "$SearchScreenNavigationRoute/{queryFromDetail}",
        arguments = listOf(
            navArgument("queryFromDetail") {
                defaultValue = ""
                type = NavType.StringType
                nullable = true
            }
        )
    ) {
        val queryFromDetail = it.arguments?.getString("queryFromDetail")
        SearchScreenRoute(onBackClick = {
            onBackClick.invoke()
        }, searchPhotoClick = {id ->
            searchPhotoClick.invoke(id)
        }, searchUserClick = {id ->
            searchUserClick.invoke(id)
        },queryFromDetail = queryFromDetail.orEmpty())
    }
}