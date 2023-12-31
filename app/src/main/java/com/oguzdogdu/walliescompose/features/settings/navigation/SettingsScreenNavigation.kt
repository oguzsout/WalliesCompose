package com.oguzdogdu.walliescompose.features.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.oguzdogdu.walliescompose.features.settings.SettingsScreenRoute

const val SettingsScreenNavigationRoute = "settings_screen_route"

fun NavController.navigateTosettingsScreen(
    navOptions: NavOptions? = null,
) {
    this.navigate(SettingsScreenNavigationRoute, navOptions)
}

fun NavGraphBuilder.settingsScreen() {
    composable(
        SettingsScreenNavigationRoute
    ) {
        SettingsScreenRoute()
    }
}