package com.oguzdogdu.walliescompose.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import com.oguzdogdu.walliescompose.features.appstate.MainAppState
import com.oguzdogdu.walliescompose.features.authenticateduser.authenticatedUserScreen
import com.oguzdogdu.walliescompose.features.authenticateduser.changeemail.changeEmailScreen
import com.oguzdogdu.walliescompose.features.authenticateduser.changeemail.navigateToChangeEmailScreen
import com.oguzdogdu.walliescompose.features.authenticateduser.changenameandsurname.changeNameAndSurnameScreen
import com.oguzdogdu.walliescompose.features.authenticateduser.changenameandsurname.navigateToChangeNameAndASurnameScreen
import com.oguzdogdu.walliescompose.features.authenticateduser.changepassword.changePasswordScreen
import com.oguzdogdu.walliescompose.features.authenticateduser.changepassword.navigateToChangePasswordScreen
import com.oguzdogdu.walliescompose.features.authenticateduser.navigateToAuthenticatedUserScreen
import com.oguzdogdu.walliescompose.features.collections.collectionScreen
import com.oguzdogdu.walliescompose.features.collections.detaillist.collectionDetailListScreen
import com.oguzdogdu.walliescompose.features.collections.detaillist.navigateToCollectionDetailListScreen
import com.oguzdogdu.walliescompose.features.detail.detailScreen
import com.oguzdogdu.walliescompose.features.detail.navigateToDetailScreen
import com.oguzdogdu.walliescompose.features.favorites.favoritesScreen
import com.oguzdogdu.walliescompose.features.favorites.navigateToFavoritesScreen
import com.oguzdogdu.walliescompose.features.home.HomeScreenNavigationRoute
import com.oguzdogdu.walliescompose.features.home.homeScreen
import com.oguzdogdu.walliescompose.features.latest.latestScreen
import com.oguzdogdu.walliescompose.features.latest.navigateToLatestScreen
import com.oguzdogdu.walliescompose.features.login.LoginScreenNavigationRoute
import com.oguzdogdu.walliescompose.features.login.forgotpassword.forgotPasswordScreen
import com.oguzdogdu.walliescompose.features.login.forgotpassword.navigateToForgotPasswordScreen
import com.oguzdogdu.walliescompose.features.login.googlesignin.GoogleAuthUiClient
import com.oguzdogdu.walliescompose.features.login.loginScreen
import com.oguzdogdu.walliescompose.features.login.signinwithemail.SignInWithEmailScreenNavigationRoute
import com.oguzdogdu.walliescompose.features.login.signinwithemail.signInWithEmailScreen
import com.oguzdogdu.walliescompose.features.popular.navigateToPopularScreen
import com.oguzdogdu.walliescompose.features.popular.popularScreen
import com.oguzdogdu.walliescompose.features.profiledetail.navigation.navigateToProfileDetailScreen
import com.oguzdogdu.walliescompose.features.profiledetail.navigation.profileDetailScreen
import com.oguzdogdu.walliescompose.features.search.navigateToSearchScreen
import com.oguzdogdu.walliescompose.features.search.searchScreen
import com.oguzdogdu.walliescompose.features.settings.settingsScreen
import com.oguzdogdu.walliescompose.features.signup.navigateToSignUpScreen
import com.oguzdogdu.walliescompose.features.signup.signUpScreen
import com.oguzdogdu.walliescompose.features.splash.SplashScreenRoute
import com.oguzdogdu.walliescompose.features.splash.splashScreen
import com.oguzdogdu.walliescompose.features.topics.navigateToTopicsScreen
import com.oguzdogdu.walliescompose.features.topics.topicdetaillist.navigateToTopicDetailListScreen
import com.oguzdogdu.walliescompose.features.topics.topicdetaillist.topicDetailListScreen
import com.oguzdogdu.walliescompose.features.topics.topicsScreen
import com.oguzdogdu.walliescompose.navigation.utils.Routes

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun WalliesNavHost(
    appState: MainAppState,
    modifier: Modifier = Modifier,
    isAuthenticated:Boolean,
    googleAuthUiClient: GoogleAuthUiClient
) {

    val navController = appState.navController
    var authState by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = isAuthenticated) {
        authState = isAuthenticated
    }
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = SplashScreenRoute,
            modifier = modifier
        ) {
            splashScreen(goToLoginFlow = {
                navController.navigate(Routes.Auth) {
                    popUpTo(SplashScreenRoute) {
                        inclusive = true
                    }
                }
            }, goToContentScreen = {
                navController.navigate(Routes.Home) {
                    popUpTo(SplashScreenRoute) {
                        inclusive = true
                    }
                }
            })

            navigation<Routes.Auth>(startDestination = LoginScreenNavigationRoute) {
                loginScreen(googleAuthUiClient = googleAuthUiClient,navigateToHome = {
                    navController.navigate(HomeScreenNavigationRoute) {
                        popUpTo(LoginScreenNavigationRoute){
                            inclusive = true
                        }
                    }
                },
                    onContinueWithoutLoginClick = {
                        navController.navigate(HomeScreenNavigationRoute) {
                            popUpTo(LoginScreenNavigationRoute){
                                inclusive = true
                            }
                        }
                    }, navigateBack = {
                        navController.navigateUp()
                    }, navigateToSignInEmail = {
                        navController.navigate(SignInWithEmailScreenNavigationRoute)
                    })

                signInWithEmailScreen(navigateToHome = {
                    navController.navigate(HomeScreenNavigationRoute) {
                        popUpTo(LoginScreenNavigationRoute){
                            inclusive = true
                        }
                    }
                }, navigateBack = {
                    navController.navigateUp()
                }, navigateToForgotPassword = {
                    navController.navigateToForgotPasswordScreen()
                },
                    navigateToSignUpScreen = {
                        navController.navigateToSignUpScreen()
                    }
                )
                forgotPasswordScreen(navigateToHome = {}, navigateBack = {
                    navController.navigateUp()
                })
                signUpScreen(navigateBack = { navController.navigateUp() }, goToLoginScreen = {
                    navController.navigate(LoginScreenNavigationRoute) {
                        popUpTo(LoginScreenNavigationRoute){
                            inclusive = true
                        }
                    }
                })
            }
            navigation<Routes.Home>(startDestination = HomeScreenNavigationRoute) {
                homeScreen(
                    transitionScope = this@SharedTransitionLayout,
                    onTopicDetailListClick = {
                        navController.navigateToTopicDetailListScreen(topicId = it)
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
                    onUserPhotoClick = {
                        navController.navigateToAuthenticatedUserScreen()
                    },
                    onRandomImageClick = {
                        navController.navigateToDetailScreen(photoId = it)
                    }
                )
                collectionScreen(
                    onCollectionClick = { id, title ->
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
                    navController.navigateUp()
                }, searchPhotoClick = {
                    navController.navigateToDetailScreen(photoId = it)
                }, searchUserClick = {
                    navController.navigateToProfileDetailScreen(username = it)
                }
                )
                detailScreen(
                    transitionScope = this@SharedTransitionLayout,
                    onBackClick = {
                        navController.navigateUp()
                    },
                    onTagClick = {
                        navController.navigateToSearchScreen(queryFromDetail = it)
                    },
                    onProfileDetailClick = {
                        navController.navigateToProfileDetailScreen(username = it)
                    },
                    onNavigateToFavoriteClick = {
                        navController.navigateToFavoritesScreen()
                    }
                )
                topicsScreen(onBackClick = {
                    navController.navigateUp()
                }, onTopicClick = {
                    navController.navigateToTopicDetailListScreen(topicId = it)
                })
                topicDetailListScreen(onTopicClick = {
                    navController.navigateToDetailScreen(photoId = it)
                }, onBackClick = {
                    navController.navigateUp()
                })
                collectionDetailListScreen(
                    transitionScope = this@SharedTransitionLayout,
                    onCollectionClick = {
                        navController.navigateToDetailScreen(photoId = it)
                    },
                    onUserDetailClick = { username ->
                        navController.navigateToProfileDetailScreen(username = username)
                    },
                    onBackClick = {
                        navController.navigateUp()
                    })
                popularScreen(
                    transitionScope = this@SharedTransitionLayout, onPopularClick = {
                        navController.navigateToDetailScreen(photoId = it)
                    },
                    onBackClick = {
                        navController.navigateUp()
                    })
                latestScreen(
                    transitionScope = this@SharedTransitionLayout,
                    onLatestClick = {
                        navController.navigateToDetailScreen(photoId = it)
                    },
                    onBackClick = {
                        navController.navigateUp()
                    })
                profileDetailScreen(onUserPhotoListClick = { id ->
                    navController.navigateToDetailScreen(photoId = id)

                }, onCollectionItemClick = { id, title ->
                    navController.navigateToCollectionDetailListScreen(
                        collectionDetailListId = id,
                        collectionDetailListTitle = title
                    )
                }, onBackClick = {
                    navController.navigateUp()
                })
                authenticatedUserScreen(
                    navigateBack = {
                        navController.navigateUp()
                    }, navigateToLogin = {
                        navController.navigate(Routes.Auth) {
                            popUpTo(Routes.Home) {
                                inclusive = true
                            }
                        }
                    }, navigateToChangeNameAndSurname = {
                        navController.navigateToChangeNameAndASurnameScreen()
                    }, navigateToChangePassword = {
                        navController.navigateToChangePasswordScreen()
                    }, navigateToChangeEmail = {
                        navController.navigateToChangeEmailScreen()
                    })
                changeNameAndSurnameScreen(navigateBack = {
                    navController.navigateUp()
                })
                changePasswordScreen(navigateBack = {
                    navController.navigateUp()
                })
                changeEmailScreen(navigateBack = {
                    navController.navigateUp()
                })
            }
        }
    }
}