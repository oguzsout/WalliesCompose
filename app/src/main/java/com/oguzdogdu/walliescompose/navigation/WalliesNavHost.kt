package com.oguzdogdu.walliescompose.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
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
import com.oguzdogdu.walliescompose.features.popular.navigateToPopularScreen
import com.oguzdogdu.walliescompose.features.popular.popularScreen
import com.oguzdogdu.walliescompose.features.search.navigateToSearchScreen
import com.oguzdogdu.walliescompose.features.search.searchScreen
import com.oguzdogdu.walliescompose.features.settings.settingsScreen
import com.oguzdogdu.walliescompose.features.topics.navigateToTopicsScreen
import com.oguzdogdu.walliescompose.features.topics.topicdetaillist.navigateToTopicDetailListScreen
import com.oguzdogdu.walliescompose.features.topics.topicdetaillist.topicDetailListScreen
import com.oguzdogdu.walliescompose.features.topics.topicsScreen

@Composable
fun WalliesNavHost(
    appState: MainAppState,
    modifier: Modifier = Modifier,
    startDestination: Boolean,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = if (!startDestination) "auth" else "content",
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

        navigation(startDestination = LoginScreenNavigationRoute, route = "auth") {
            loginScreen(navigateToHome = {
                navController.navigate(HomeScreenNavigationRoute)
            },
                onContinueWithoutLoginClick = {
                    navController.navigate("content") {
                        popUpTo("auth") {
                            inclusive = true
                        }
                    }
                })
        }
        navigation(startDestination = HomeScreenNavigationRoute, route = "content") {
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
