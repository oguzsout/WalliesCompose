package com.oguzdogdu.walliescompose.features.settings

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.oguzdogdu.walliescompose.navigation.Screens

fun NavController.navigateToSettingsScreen(
    navOptions: NavOptions? = null,
) {
    navigate(route = Screens.SettingsScreenNavigationRoute, navOptions)
}

fun NavGraphBuilder.settingsScreen() {
    composable<Screens.SettingsScreenNavigationRoute>{
        val viewModel : SettingsViewModel = hiltViewModel()
        val settingsUiState by viewModel.settingsState.collectAsStateWithLifecycle()
        SettingsScreenRoute(state = settingsUiState, onSettingsScreenEvent = viewModel::handleScreenEvents)
    }
}