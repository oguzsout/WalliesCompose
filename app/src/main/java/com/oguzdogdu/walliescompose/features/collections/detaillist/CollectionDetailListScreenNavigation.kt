package com.oguzdogdu.walliescompose.features.collections.detaillist

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

fun NavGraphBuilder.collectionDetailListScreen(
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
        CollectionDetailListScreenRoute(
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