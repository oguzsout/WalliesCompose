package com.oguzdogdu.walliescompose.features.appstate

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.oguzdogdu.walliescompose.navigation.TopLevelDestination
import com.oguzdogdu.walliescompose.navigation.WalliesNavHost

@Composable
fun WalliesApp(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    appState: MainAppState = rememberMainAppState(windowSizeClass = windowSizeClass),
) {
    Scaffold(modifier = modifier, bottomBar = {
        AppNavBar(
                destinations = AppDestinations(appState.topLevelDestinations),
                onNavigateToDestination = appState::navigateToTopLevelDestination,
                currentDestination = appState.currentDestination
            )

    }) {
        WalliesNavHost(
            navController = appState.navController, modifier = modifier.padding(it)
        )
    }
}

data class AppDestinations(
    val destinations: List<TopLevelDestination>,
) : List<TopLevelDestination> by destinations

@Composable
internal fun AppNavBar(
    destinations: AppDestinations,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
) {
    NavigationBar {
        destinations.forEach { destination ->
            val selected =
                currentDestination?.hierarchy?.any { it.route == destination.route } == true
            NavigationBarItem(selected = selected,
                label = { Text(text = stringResource(id = destination.titleTextId)) },
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        painterResource(id = destination.icon),
                        contentDescription = null,
                    )
                }
            )
        }
    }
}