package com.oguzdogdu.walliescompose.features.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object SettingsScreenNavigationRoute

fun NavController.navigateToSettingsScreen(
    navOptions: NavOptions? = null,
) {
    navigate(route = SettingsScreenNavigationRoute, navOptions)
}

fun NavGraphBuilder.settingsScreen() {
    composable<SettingsScreenNavigationRoute >{
        SettingsScreenRoute()
    }
}