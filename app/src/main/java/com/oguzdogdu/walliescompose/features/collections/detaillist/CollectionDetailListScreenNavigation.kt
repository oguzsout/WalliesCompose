package com.oguzdogdu.walliescompose.features.collections.detaillist

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.oguzdogdu.walliescompose.navigation.Screens


fun NavController.navigateToCollectionDetailListScreen(
    collectionDetailListId: String?,
    collectionDetailListTitle: String?,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(
        route = Screens.CollectionDetailListScreenRoute(
            collectionDetailListId,
            collectionDetailListTitle
        )
    ) {
        navOptions()
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.collectionDetailListScreen(
    transitionScope: SharedTransitionScope,
    onBackClick: () -> Unit,
    onCollectionClick: (String) -> Unit,
    onUserDetailClick: (String) -> Unit
) {
    composable<Screens.CollectionDetailListScreenRoute> { backStackEntry ->

        val collectionDetailListArgs = backStackEntry.toRoute<Screens.CollectionDetailListScreenRoute>()

        transitionScope.CollectionDetailListScreenRoute(
            animatedVisibilityScope = this,
            collectionDetailId = collectionDetailListArgs.collectionDetailListId,
            collectionDetailTitle = collectionDetailListArgs.collectionDetailListTitle,
            onCollectionClick = { id ->
                onCollectionClick.invoke(id)
            },
            onUserDetailClick = { username ->
                onUserDetailClick.invoke(username)
            },
            onBackClick = {
                onBackClick.invoke()
            }
        )
    }
}