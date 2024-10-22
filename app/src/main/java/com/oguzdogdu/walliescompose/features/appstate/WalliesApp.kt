package com.oguzdogdu.walliescompose.features.appstate

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.get
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.features.login.googlesignin.GoogleAuthUiClient
import com.oguzdogdu.walliescompose.navigation.NavigationBarGraph
import com.oguzdogdu.walliescompose.navigation.TopLevelDestination
import com.oguzdogdu.walliescompose.navigation.WalliesNavHost
import com.oguzdogdu.walliescompose.ui.theme.medium
import com.oguzdogdu.walliescompose.util.NavigationScreenRouteClassNames
import com.oguzdogdu.walliescompose.util.NetworkMonitor
import com.oguzdogdu.walliescompose.util.currentRouteClassName
import com.oguzdogdu.walliescompose.util.routeClassName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun WalliesApp(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    networkMonitor: NetworkMonitor,
    googleAuthUiClient: GoogleAuthUiClient,
    appState: MainAppState = rememberMainAppState(
        coroutineScope = coroutineScope,
        networkMonitor = networkMonitor
    )
) {
    val topLevelDestination = listOf(
        TopLevelDestination.HomeScreenNavigationRoute,
        TopLevelDestination.CollectionScreenNavigationRoute,
        TopLevelDestination.FavoritesScreenNavigationRoute,
        TopLevelDestination.SettingsScreenNavigationRoute
    )
    val navController = rememberNavController()

    val isOffline by appState.isOffline.collectAsStateWithLifecycle()

    var snackbarModel by remember { mutableStateOf<SnackbarModel?>(null) }

    LaunchedEffect(isOffline) {
        if (isOffline) {
            snackbarModel = SnackbarModel(
                type = MessageType.ERROR,
                drawableRes = R.drawable.ic_cancel,
                message = MessageContent.ResourceString(R.string.internet_error),
                duration = Duration.SHORT
            )
        }
    }
    SharedTransitionLayout {
        Scaffold(
            modifier = Modifier.fillMaxSize().skipToLookaheadSize(),
            bottomBar = {
                WalliesBottomAppBar(
                    navController = navController,
                    bottomBarItems = topLevelDestination
                )
            },
            snackbarHost = {
                CustomSnackbar(snackbarModel = snackbarModel)
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                WalliesNavHost(
                    navController = navController,
                    googleAuthUiClient = googleAuthUiClient
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.WalliesBottomAppBar(
    navController: NavController,
    bottomBarItems: List<TopLevelDestination>,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = navController.currentRouteClassName in NavigationScreenRouteClassNames
    ) {
        NavigationBar(modifier = modifier
            .renderInSharedTransitionScopeOverlay(
                zIndexInOverlay = 1f,
            )
            .animateEnterExit(
                enter = fadeIn() + slideInHorizontally(),
                exit = fadeOut() + slideOutHorizontally()
            )) {
            bottomBarItems.forEach { destination ->
                NavigationBarItem(
                    selected = navController.currentRouteClassName == destination.routeClassName,
                    onClick = {
                        navController.navigate(destination.route) {
                            popUpTo(navController.graph.get(NavigationBarGraph).id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }

                    },
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = destination.icon),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(id = destination.iconTextId),
                            fontSize = 11.sp,
                            fontFamily = medium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.onPrimary
                    ),
                )
            }
        }
    }
}