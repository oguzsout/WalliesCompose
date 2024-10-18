package com.oguzdogdu.walliescompose.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
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
    googleAuthUiClient: GoogleAuthUiClient
) {
    val navController = appState.navController

    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = SplashScreenRoute,
            modifier = modifier
        ) {
            splashScreen(goToLoginFlow = {
                navController.navigate(AuthGraph) {
                    popUpTo(SplashScreenRoute) {
                        inclusive = true
                    }
                }
            }, goToContentScreen = {
                navController.navigate(HomeGraph) {
                    popUpTo(SplashScreenRoute) {
                        inclusive = true
                    }
                }
            })
            navigationAuthGraph(
                navHostController = navController,
                googleAuthUiClient = googleAuthUiClient
            )
            navigationHomeGraph(
                navHostController = navController,
                sharedTransitionScope = this@SharedTransitionLayout
            )
        }
    }
}