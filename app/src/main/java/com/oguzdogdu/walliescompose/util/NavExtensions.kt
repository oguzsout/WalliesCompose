package com.oguzdogdu.walliescompose.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.oguzdogdu.walliescompose.navigation.TopLevelDestination

typealias RouteClassName = String

val NavigationScreenRouteClassNames: List<RouteClassName?> by lazy {
    TopLevelDestination.entries.map { it.route::class.simpleName }
}

val NavController.currentRouteClassName: RouteClassName?
    @Composable
    get() {
        val navBackStackEntry by this.currentBackStackEntryAsState()
        return navBackStackEntry?.destination?.route
            ?.substringBefore("?")
            ?.substringBefore("/")
            ?.substringAfterLast(".")
    }

val TopLevelDestination.routeClassName: RouteClassName?
    get() = this.route::class.simpleName