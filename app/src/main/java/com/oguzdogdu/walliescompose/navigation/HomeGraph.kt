package com.oguzdogdu.walliescompose.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
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
import com.oguzdogdu.walliescompose.features.home.homeScreen
import com.oguzdogdu.walliescompose.features.latest.latestScreen
import com.oguzdogdu.walliescompose.features.popular.navigateToPopularScreen
import com.oguzdogdu.walliescompose.features.popular.popularScreen
import com.oguzdogdu.walliescompose.features.profiledetail.navigation.navigateToProfileDetailScreen
import com.oguzdogdu.walliescompose.features.profiledetail.navigation.profileDetailScreen
import com.oguzdogdu.walliescompose.features.search.navigateToSearchScreen
import com.oguzdogdu.walliescompose.features.search.searchScreen
import com.oguzdogdu.walliescompose.features.topics.navigateToTopicsScreen
import com.oguzdogdu.walliescompose.features.topics.topicdetaillist.navigateToTopicDetailListScreen
import com.oguzdogdu.walliescompose.features.topics.topicdetaillist.topicDetailListScreen
import com.oguzdogdu.walliescompose.features.topics.topicsScreen
import kotlinx.serialization.Serializable

@Serializable
object HomeGraph

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.navigationHomeGraph(
    navHostController: NavHostController,
    sharedTransitionScope: SharedTransitionScope
) {
    navigation<HomeGraph>(startDestination = Screens.HomeScreenNavigationRoute) {
        homeScreen(
            transitionScope = sharedTransitionScope,
            onTopicDetailListClick = {
                navHostController.navigateToTopicDetailListScreen(topicId = it)
            },
            onPopularClick = {
                navHostController.navigateToDetailScreen(photoId = it)
            },
            onTopicSeeAllClick = {
                navHostController.navigateToTopicsScreen()
            },
            onSearchClick = {
                navHostController.navigateToSearchScreen()
            },
            onPopularSeeAllClick = {
                navHostController.navigateToPopularScreen()
            },
            onUserPhotoClick = {
                navHostController.navigateToAuthenticatedUserScreen()
            },
            onRandomImageClick = {
                navHostController.navigateToDetailScreen(photoId = it)
            }
        )

        searchScreen(onBackClick = {
            navHostController.navigateUp()
        }, searchPhotoClick = {
            navHostController.navigateToDetailScreen(photoId = it)
        }, searchUserClick = {
            navHostController.navigateToProfileDetailScreen(username = it)
        }
        )
        detailScreen(
            transitionScope = sharedTransitionScope,
            onBackClick = {
                navHostController.navigateUp()
            },
            onTagClick = {
                navHostController.navigateToSearchScreen(queryFromDetail = it)
            },
            onProfileDetailClick = {
                navHostController.navigateToProfileDetailScreen(username = it)
            },
            onNavigateToFavoriteClick = {
                navHostController.navigateToFavoritesScreen()
            }
        )
        topicsScreen(onBackClick = {
            navHostController.navigateUp()
        }, onTopicClick = {
            navHostController.navigateToTopicDetailListScreen(topicId = it)
        })
        topicDetailListScreen(onTopicClick = {
            navHostController.navigateToDetailScreen(photoId = it)
        }, onBackClick = {
            navHostController.navigateUp()
        })
        collectionDetailListScreen(
            transitionScope = sharedTransitionScope,
            onCollectionClick = {
                navHostController.navigateToDetailScreen(photoId = it)
            },
            onUserDetailClick = { username ->
                navHostController.navigateToProfileDetailScreen(username = username)
            },
            onBackClick = {
                navHostController.navigateUp()
            })
        popularScreen(
            transitionScope = sharedTransitionScope, onPopularClick = {
                navHostController.navigateToDetailScreen(photoId = it)
            },
            onBackClick = {
                navHostController.navigateUp()
            })
        latestScreen(
            transitionScope = sharedTransitionScope,
            onLatestClick = {
                navHostController.navigateToDetailScreen(photoId = it)
            },
            onBackClick = {
                navHostController.navigateUp()
            })
        profileDetailScreen(onUserPhotoListClick = { id ->
            navHostController.navigateToDetailScreen(photoId = id)

        }, onCollectionItemClick = { id, title ->
            navHostController.navigateToCollectionDetailListScreen(
                collectionDetailListId = id,
                collectionDetailListTitle = title
            )
        }, onBackClick = {
            navHostController.navigateUp()
        })
        authenticatedUserScreen(
            navigateBack = {
                navHostController.navigateUp()
            }, navigateToLogin = {
                navHostController.navigate(AuthGraph) {
                    popUpTo(HomeGraph) {
                        inclusive = true
                    }
                }
            }, navigateToChangeNameAndSurname = {
                navHostController.navigateToChangeNameAndASurnameScreen()
            }, navigateToChangePassword = {
                navHostController.navigateToChangePasswordScreen()
            }, navigateToChangeEmail = {
                navHostController.navigateToChangeEmailScreen()
            })
        changeNameAndSurnameScreen(navigateBack = {
            navHostController.navigateUp()
        })
        changePasswordScreen(navigateBack = {
            navHostController.navigateUp()
        })
        changeEmailScreen(navigateBack = {
            navHostController.navigateUp()
        })
    }
}