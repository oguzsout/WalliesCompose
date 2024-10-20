package com.oguzdogdu.walliescompose.features.detail

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.oguzdogdu.walliescompose.navigation.Screens

fun NavController.navigateToDetailScreen(
    photoId: String?,

) {
    navigate(route = Screens.DetailScreenRoute(photoId))
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.detailScreen(
    transitionScope: SharedTransitionScope,
    onBackClick: () -> Unit,
    onTagClick: (String) -> Unit,
    onProfileDetailClick: (String) -> Unit,
    onNavigateToFavoriteClick: () -> Unit
) {
    composable<Screens.DetailScreenRoute>{
        transitionScope.DetailScreenRoute(
            animatedVisibilityScope = this,
            onBackClick = onBackClick,
            onProfileDetailClick = { username ->
                onProfileDetailClick.invoke(username)
            },
            onTagClick = { tag ->
                onTagClick.invoke(tag)
            },
            onNavigateToFavorite = onNavigateToFavoriteClick
        )
    }
}