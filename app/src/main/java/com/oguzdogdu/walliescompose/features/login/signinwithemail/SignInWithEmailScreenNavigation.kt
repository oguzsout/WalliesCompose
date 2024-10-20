package com.oguzdogdu.walliescompose.features.login.signinwithemail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.oguzdogdu.walliescompose.navigation.Screens

fun NavController.navigateToSignInWithEmailScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = Screens.SignInWithEmailScreenNavigationRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.signInWithEmailScreen(
    navigateToHome: () -> Unit,
    navigateBack: () -> Unit,
    navigateToForgotPassword: () -> Unit,
    navigateToSignUpScreen: () -> Unit,
) {
    composable<Screens.SignInWithEmailScreenNavigationRoute>{
        SignInWithEmailScreenRoute(
            navigateToHome = { navigateToHome.invoke() },
            navigateToSignUpScreen = { navigateToSignUpScreen.invoke() },
            navigateBack = { navigateBack.invoke() },
            navigateToForgotPassword = { navigateToForgotPassword.invoke() }
        )
    }
}