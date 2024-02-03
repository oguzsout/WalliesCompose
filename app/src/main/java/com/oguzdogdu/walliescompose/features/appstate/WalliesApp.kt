package com.oguzdogdu.walliescompose.features.appstate

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.oguzdogdu.walliescompose.features.home.HomeScreenNavigationRoute
import com.oguzdogdu.walliescompose.features.splash.SplashScreenNavigationRoute
import com.oguzdogdu.walliescompose.navigation.TopLevelDestination
import com.oguzdogdu.walliescompose.navigation.WalliesNavHost
import com.oguzdogdu.walliescompose.ui.theme.medium

@Composable
fun WalliesApp(
    modifier: Modifier = Modifier,
    appState: MainAppState = rememberMainAppState(),
    isAuthenticated:Boolean,
) {
    Scaffold(modifier = modifier, bottomBar = {
        if (appState.shouldShowBottomBar) {
            AppNavBar(
                destinations = appState.topLevelDestinations,
                onNavigateToDestination = appState::navigateToTopLevelDestination,
                currentDestination = appState.currentDestination
            )
        }
    }) {
        WalliesNavHost(
            appState = appState, modifier = modifier.padding(it), isAuthenticated = isAuthenticated
        )
    }
}

@Composable
internal fun AppNavBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
) {
    NavigationBar {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            NavigationBarItem(
                alwaysShowLabel = true,
                selected = selected,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.onPrimary
                ),
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = destination.icon),
                        contentDescription = ""
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = destination.iconTextId),
                        fontSize = 11.sp,
                        fontFamily = medium,
                        color = Color.Unspecified
                    )
                }
            )
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.route, true) ?: false
    } ?: false