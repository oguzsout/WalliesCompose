package com.oguzdogdu.walliescompose.features.profiledetail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.oguzdogdu.walliescompose.features.profiledetail.ProfileDetailScreenRoute

const val ProfileDetailScreenNavigationRoute = "profile_detail_screen_route"

fun NavController.navigateToProfileDetailScreen(
    username: String?,
    navOptions: NavOptions? = null,
) {
    this.navigate("$ProfileDetailScreenNavigationRoute/$username", navOptions)
}

fun NavGraphBuilder.profileDetailScreen(
    onUserPhotoListClick: (String) -> Unit,
    onCollectionItemClick: (String, String) -> Unit,
    onBackClick: () -> Unit
) {
    composable(
        "$ProfileDetailScreenNavigationRoute/{username}",
        arguments = listOf(
            navArgument("username") {
                defaultValue = ""
                type = NavType.StringType
                nullable = true
            }
        )
    ) {
        ProfileDetailScreenRoute(onUserPhotoListClick = { id ->
            onUserPhotoListClick.invoke(id) },
            onCollectionItemClick = { id, title ->
            onCollectionItemClick.invoke(id, title)
        }, onBackClick = {
            onBackClick.invoke()
        })
    }
}