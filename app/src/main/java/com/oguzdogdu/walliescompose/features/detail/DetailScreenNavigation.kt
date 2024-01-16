package com.oguzdogdu.walliescompose.features.detail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument


const val DetailScreenNavigationRoute = "detail_screen_route"

fun NavController.navigateToDetailScreen(
    photoId: String?,
    navOptions: NavOptions? = null,
) {
    this.navigate("$DetailScreenNavigationRoute/$photoId", navOptions)
}

fun NavGraphBuilder.detailScreen(onBackClick: () -> Unit) {
    composable(
        "$DetailScreenNavigationRoute/{photoId}",
        arguments = listOf(
            navArgument("photoId") {
                type = NavType.StringType
            }
        )
    ) {
        DetailScreenRoute(
            onBackClick = onBackClick,
            onProfileDetailClick = {
                println(it)
            }
        )
    }
}