package com.oguzdogdu.walliescompose.features.collections.detaillist

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data class CollectionDetailListScreenRoute(
    val collectionDetailListId: String? = null,
    val collectionDetailListTitle: String? = null,
)

fun NavController.navigateToCollectionDetailListScreen(
    collectionDetailListId: String?,
    collectionDetailListTitle: String?,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(
        route = CollectionDetailListScreenRoute(
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
    onCollectionClick: (String) -> Unit
) {
    composable<CollectionDetailListScreenRoute> { backStackEntry ->

        val collectionDetailListArgs = backStackEntry.toRoute<CollectionDetailListScreenRoute>()

        transitionScope.CollectionDetailListScreenRoute(
            animatedVisibilityScope = this,
            collectionDetailId = collectionDetailListArgs.collectionDetailListId,
            collectionDetailTitle = collectionDetailListArgs.collectionDetailListTitle,
            onCollectionClick = { id ->
                onCollectionClick.invoke(id)
            },
            onBackClick = {
                onBackClick.invoke()
            })
    }
}