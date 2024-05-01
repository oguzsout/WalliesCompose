package com.oguzdogdu.walliescompose.features.collections.detaillist

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val CollectionDetailListScreenNavigationRoute = "collection_detail_list_screen_route"

fun NavController.navigateToCollectionDetailListScreen(
    collectionDetailListId: String?,
    collectionDetailListTitle: String?,
    navOptions: NavOptions? = null,
) {
    this.navigate("$CollectionDetailListScreenNavigationRoute/$collectionDetailListId/$collectionDetailListTitle", navOptions)
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.collectionDetailListScreen(
    transitionScope: SharedTransitionScope,
    onBackClick: () -> Unit,
    onCollectionClick: (String) -> Unit) {
    composable(
        "$CollectionDetailListScreenNavigationRoute/{collectionDetailListId}/{collectionDetailListTitle}",
        arguments = listOf(
            navArgument("collectionDetailListId") {
                type = NavType.StringType
            }
            ,navArgument("collectionDetailListTitle") {
                type = NavType.StringType
            }
        )
    ) { it ->
        val collectionDetailListId = it.arguments?.getString("collectionDetailListId","")
        val collectionDetailListTitle = it.arguments?.getString("collectionDetailListTitle","")
        transitionScope.CollectionDetailListScreenRoute(
            animatedVisibilityScope = this,
            collectionDetailId = collectionDetailListId.orEmpty(),
            collectionDetailTitle = collectionDetailListTitle.orEmpty(),
            onCollectionClick = {id ->
                onCollectionClick.invoke(id)
            },
            onBackClick = {
                onBackClick.invoke()
            })
    }
}