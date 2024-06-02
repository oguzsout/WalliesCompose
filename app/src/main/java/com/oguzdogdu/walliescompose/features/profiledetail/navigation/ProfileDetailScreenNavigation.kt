package com.oguzdogdu.walliescompose.features.profiledetail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.oguzdogdu.walliescompose.features.profiledetail.ProfileDetailScreenRoute
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDetailScreenNavigationRoute(val username: String? = null)

fun NavController.navigateToProfileDetailScreen(
    username: String?,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = ProfileDetailScreenNavigationRoute(username)) {
        navOptions()
    }
}

fun NavGraphBuilder.profileDetailScreen(
    onUserPhotoListClick: (String) -> Unit,
    onCollectionItemClick: (String, String) -> Unit,
    onBackClick: () -> Unit
) {
    composable<ProfileDetailScreenNavigationRoute> {
        ProfileDetailScreenRoute(onUserPhotoListClick = { id ->
            onUserPhotoListClick.invoke(id) },
            onCollectionItemClick = { id, title ->
            onCollectionItemClick.invoke(id, title)
        }, onBackClick = {
            onBackClick.invoke()
        })
    }
}