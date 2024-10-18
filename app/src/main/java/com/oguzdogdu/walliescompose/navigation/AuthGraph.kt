package com.oguzdogdu.walliescompose.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.oguzdogdu.walliescompose.features.login.LoginScreenNavigationRoute
import com.oguzdogdu.walliescompose.features.login.forgotpassword.forgotPasswordScreen
import com.oguzdogdu.walliescompose.features.login.forgotpassword.navigateToForgotPasswordScreen
import com.oguzdogdu.walliescompose.features.login.googlesignin.GoogleAuthUiClient
import com.oguzdogdu.walliescompose.features.login.loginScreen
import com.oguzdogdu.walliescompose.features.login.signinwithemail.SignInWithEmailScreenNavigationRoute
import com.oguzdogdu.walliescompose.features.login.signinwithemail.signInWithEmailScreen
import com.oguzdogdu.walliescompose.features.signup.navigateToSignUpScreen
import com.oguzdogdu.walliescompose.features.signup.signUpScreen
import kotlinx.serialization.Serializable

@Serializable
object AuthGraph

fun NavGraphBuilder.navigationAuthGraph(
    navHostController: NavHostController,
    googleAuthUiClient: GoogleAuthUiClient
) {
    navigation<AuthGraph>(startDestination = LoginScreenNavigationRoute) {
        loginScreen(googleAuthUiClient = googleAuthUiClient, navigateToHome = {
            navHostController.navigate(HomeGraph) {
                popUpTo(AuthGraph) {
                    inclusive = true
                }
            }
        },
            onContinueWithoutLoginClick = {
                navHostController.navigate(HomeGraph) {
                    popUpTo(AuthGraph) {
                        inclusive = true
                    }
                }
            }, navigateBack = {
                navHostController.navigateUp()
            }, navigateToSignInEmail = {
                navHostController.navigate(SignInWithEmailScreenNavigationRoute)
            })

        signInWithEmailScreen(navigateToHome = {
            navHostController.navigate(HomeGraph) {
                popUpTo(AuthGraph) {
                    inclusive = true
                }
            }
        }, navigateBack = {
            navHostController.navigateUp()
        }, navigateToForgotPassword = {
            navHostController.navigateToForgotPasswordScreen()
        },
            navigateToSignUpScreen = {
                navHostController.navigateToSignUpScreen()
            }
        )
        forgotPasswordScreen(navigateToHome = {}, navigateBack = {
            navHostController.navigateUp()
        })
        signUpScreen(navigateBack = { navHostController.navigateUp() }, goToLoginScreen = {
            navHostController.navigate(LoginScreenNavigationRoute) {
                popUpTo(LoginScreenNavigationRoute) {
                    inclusive = true
                }
            }
        })
    }
}