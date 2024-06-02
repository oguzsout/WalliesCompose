package com.oguzdogdu.walliescompose.features.authenticateduser

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object AuthenticatedUserScreenRoute

fun NavController.navigateToAuthenticatedUserScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = AuthenticatedUserScreenRoute) {
        navOptions()
    }
 }

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.authenticatedUserScreen(
    transitionScope: SharedTransitionScope,
    navigateBack: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToChangeNameAndSurname: () -> Unit,
    navigateToChangePassword: () -> Unit,
    navigateToChangeEmail: () -> Unit,

    ) {
    composable<AuthenticatedUserScreenRoute> {
        transitionScope.AuthenticatedUserScreenRoute(
            animatedVisibilityScope = this,
           navigateBack = {
               navigateBack.invoke()
           },
            navigateToLogin = {
                navigateToLogin.invoke()
            },
            navigateToChangeNameAndSurname = {
                navigateToChangeNameAndSurname.invoke()
            },
            navigateToChangePassword = {
                navigateToChangePassword.invoke()
            }, navigateToChangeEmail = {
                navigateToChangeEmail.invoke()
            }
        )
    }
}