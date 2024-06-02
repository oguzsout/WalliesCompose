package com.oguzdogdu.walliescompose.navigation.utils

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes(val route : String) {
    @Serializable
    data object Auth : Routes("auth")
    @Serializable
    data object Home : Routes("home")
}