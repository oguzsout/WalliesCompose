package com.oguzdogdu.walliescompose.features.appstate

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.features.login.googlesignin.GoogleAuthUiClient
import com.oguzdogdu.walliescompose.navigation.TopLevelDestination
import com.oguzdogdu.walliescompose.navigation.WalliesNavHost
import com.oguzdogdu.walliescompose.ui.theme.medium
import com.oguzdogdu.walliescompose.util.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun WalliesApp(
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    networkMonitor: NetworkMonitor,
    googleAuthUiClient: GoogleAuthUiClient,
    appState: MainAppState = rememberMainAppState(
        coroutineScope = coroutineScope,
        networkMonitor = networkMonitor
    ),
) {
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
        Scaffold(modifier = modifier.fillMaxSize(), bottomBar = {
            AnimatedVisibility(
                visible = appState.shouldShowBottomBar
            ) {
                AppNavBar(
                    destinations = appState.topLevelDestinations,
                    onNavigateToDestination = { appState.navigateToTopLevelDestination(it) },
                    currentDestination = appState.currentDestination,
                    modifier = Modifier
                        .renderInSharedTransitionScopeOverlay()
                        .animateEnterExit(
                            enter = fadeIn() + slideInVertically {
                                it
                            },
                            exit = fadeOut() + slideOutVertically {
                                it
                            }
                        )
                )
            }
        }, snackbarHost = {
            CustomSnackbar(snackbarModel = snackbarModel, onDismiss = {
                coroutineScope.launch {
                    snackbarModel = null
                }
            })
        }) {
            WalliesNavHost(
                appState = appState,
                modifier = Modifier.padding(it),
                googleAuthUiClient = googleAuthUiClient
            )
        }
    }
}

@Composable
internal fun AppNavBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            NavigationBarItem(
                alwaysShowLabel = true,
                selected = selected,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.onPrimary
                ),
                onClick = {
                    if (!selected) {
                        onNavigateToDestination(destination)
                    }
                },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = destination.icon),
                        contentDescription = "",
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
                }
            )
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.hasRoute(destination.route)
    } ?: false