package com.oguzdogdu.walliescompose.features.profiledetail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.oguzdogdu.walliescompose.features.profiledetail.ProfileDetailScreenRoute
import com.oguzdogdu.walliescompose.navigation.Screens

fun NavController.navigateToProfileDetailScreen(
    username: String?,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = Screens.ProfileDetailScreenNavigationRoute(username)) {
        navOptions()
    }
}

fun NavGraphBuilder.profileDetailScreen(
    onUserPhotoListClick: (String) -> Unit,
    onCollectionItemClick: (String, String) -> Unit,
    onBackClick: () -> Unit
) {
    composable<Screens.ProfileDetailScreenNavigationRoute> {
        ProfileDetailScreenRoute(onUserPhotoListClick = { id ->
            onUserPhotoListClick.invoke(id) },
            onCollectionItemClick = { id, title ->
            onCollectionItemClick.invoke(id, title)
        }, onBackClick = {
            onBackClick.invoke()
        })
    }
}