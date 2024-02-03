package com.oguzdogdu.walliescompose.navigation

import android.app.Activity
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import androidx.navigation.navOptions
import com.oguzdogdu.walliescompose.features.appstate.MainAppState
import com.oguzdogdu.walliescompose.features.collections.collectionScreen
import com.oguzdogdu.walliescompose.features.collections.detaillist.collectionDetailListScreen
import com.oguzdogdu.walliescompose.features.collections.detaillist.navigateToCollectionDetailListScreen
import com.oguzdogdu.walliescompose.features.detail.detailScreen
import com.oguzdogdu.walliescompose.features.detail.navigateToDetailScreen
import com.oguzdogdu.walliescompose.features.favorites.favoritesScreen
import com.oguzdogdu.walliescompose.features.home.HomeScreenNavigationRoute
import com.oguzdogdu.walliescompose.features.home.homeScreen
import com.oguzdogdu.walliescompose.features.home.navigateToHomeScreen
import com.oguzdogdu.walliescompose.features.latest.latestScreen
import com.oguzdogdu.walliescompose.features.latest.navigateToLatestScreen
import com.oguzdogdu.walliescompose.features.login.LoginScreenNavigationRoute
import com.oguzdogdu.walliescompose.features.login.loginScreen
import com.oguzdogdu.walliescompose.features.main.MainActivity
import com.oguzdogdu.walliescompose.features.popular.navigateToPopularScreen
import com.oguzdogdu.walliescompose.features.popular.popularScreen
import com.oguzdogdu.walliescompose.features.search.navigateToSearchScreen
import com.oguzdogdu.walliescompose.features.search.searchScreen
import com.oguzdogdu.walliescompose.features.settings.settingsScreen
import com.oguzdogdu.walliescompose.features.splash.SplashScreenNavigationRoute
import com.oguzdogdu.walliescompose.features.splash.navigateToSplashScreen
import com.oguzdogdu.walliescompose.features.splash.splashScreen
import com.oguzdogdu.walliescompose.features.topics.navigateToTopicsScreen
import com.oguzdogdu.walliescompose.features.topics.topicdetaillist.navigateToTopicDetailListScreen
import com.oguzdogdu.walliescompose.features.topics.topicdetaillist.topicDetailListScreen
import com.oguzdogdu.walliescompose.features.topics.topicsScreen
import com.oguzdogdu.walliescompose.navigation.utils.NavigationConstants.AUTH
import com.oguzdogdu.walliescompose.navigation.utils.NavigationConstants.CONTENT


@Composable
fun WalliesNavHost(
    appState: MainAppState,
    modifier: Modifier = Modifier,
    startDestination: String = SplashScreenNavigationRoute,
    isAuthenticated:Boolean
) {
    val context = LocalContext.current
    val navController = appState.navController
    val authState by remember {
        mutableStateOf(isAuthenticated)
    }

        NavHost(
        navController = navController,
        startDestination = determineStartDestination(authState, startDestination),
        modifier = modifier,
        enterTransition = {
            EnterTransition.None
        }, exitTransition = {
            ExitTransition.None
        }, popEnterTransition = {
            EnterTransition.None
        }, popExitTransition = {
            ExitTransition.None
        }
    ) {
            splashScreen(goToLoginFlow = {
                navController.navigate(LoginScreenNavigationRoute)
            }, goToContentScreen = {
                navController.navigate(HomeScreenNavigationRoute)
            })
        navigation(startDestination = LoginScreenNavigationRoute, route = AUTH,enterTransition = {
            EnterTransition.None
        }, exitTransition = {
            ExitTransition.None
        }, popEnterTransition = {
            EnterTransition.None
        }, popExitTransition = {
            ExitTransition.None
        }) {
            loginScreen(navigateToHome = {
                navController.navigate(HomeScreenNavigationRoute) {
                    popUpTo(SplashScreenNavigationRoute){
                        inclusive = true
                    }
                }
            },
                onContinueWithoutLoginClick = {
                    navController.navigate(HomeScreenNavigationRoute) {
                        popUpTo(SplashScreenNavigationRoute){
                            inclusive = true
                        }
                    }
                }, navigateBack = {
                    val activity = context as? MainActivity
                    activity?.finish()
                })
        }

        navigation(startDestination = HomeScreenNavigationRoute, route = CONTENT,enterTransition = {
            EnterTransition.None
        }, exitTransition = {
            ExitTransition.None
        }, popEnterTransition = {
            EnterTransition.None
        }, popExitTransition = {
            ExitTransition.None
        }) {
            homeScreen(
                onTopicDetailListClick = {
                    navController.navigateToTopicDetailListScreen(topicId = it)
                },
                onLatestClick = {
                    navController.navigateToDetailScreen(photoId = it)
                },
                onPopularClick = {
                    navController.navigateToDetailScreen(photoId = it)
                },
                onTopicSeeAllClick = {
                    navController.navigateToTopicsScreen()
                },
                onSearchClick = {
                    navController.navigateToSearchScreen()
                },
                onPopularSeeAllClick = {
                    navController.navigateToPopularScreen()
                },
                onLatestSeeAllClick = {
                    navController.navigateToLatestScreen()
                }
            )
            collectionScreen(onCollectionClick = {id,title ->
                navController.navigateToCollectionDetailListScreen(
                    collectionDetailListId = id,
                    collectionDetailListTitle = title
                )
            })
            favoritesScreen(onFavoriteClick = {
                navController.navigateToDetailScreen(photoId = it)
            })
            settingsScreen()
            searchScreen(onBackClick = {
                navController.popBackStack()
            }, searchPhotoClick = {
                navController.navigateToDetailScreen(photoId = it)
            })
            detailScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onTagClick = {
                    navController.navigateToSearchScreen(queryFromDetail = it)
                }
            )
            topicsScreen(onBackClick = {
                navController.navigateToHomeScreen()
            }, onTopicClick = {
                navController.navigateToTopicDetailListScreen(topicId = it)
            })
            topicDetailListScreen(onTopicClick = {
                navController.navigateToDetailScreen(photoId = it)
            }, onBackClick = {
                navController.popBackStack()
            })
            collectionDetailListScreen(onCollectionClick = {
                navController.navigateToDetailScreen(photoId = it)
            }, onBackClick = {
                navController.popBackStack()
            })
            popularScreen(onPopularClick = {
                navController.navigateToDetailScreen(photoId = it)
            },
                onBackClick = {
                    navController.popBackStack()
                })
            latestScreen(onLatestClick = {
                navController.navigateToDetailScreen(photoId = it)
            },
                onBackClick = {
                    navController.popBackStack()
                })
        }
    }
}

private fun determineStartDestination(isLoggedIn: Boolean, default: String): String {
    return if (isLoggedIn) {
        HomeScreenNavigationRoute
    } else {
        default
    }
}