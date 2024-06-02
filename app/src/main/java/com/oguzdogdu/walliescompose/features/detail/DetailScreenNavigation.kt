package com.oguzdogdu.walliescompose.features.detail

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class DetailScreenRoute(val photoId: String? = null)

fun NavController.navigateToDetailScreen(
    photoId: String?,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = DetailScreenRoute(photoId)) {
        navOptions()
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.detailScreen(
    transitionScope: SharedTransitionScope,
    onBackClick: () -> Unit,
    onTagClick: (String) -> Unit,
    onProfileDetailClick: (String) -> Unit
) {
    composable<DetailScreenRoute>{
       val detailViewModel: DetailViewModel = hiltViewModel()
        transitionScope.DetailScreenRoute(
            animatedVisibilityScope = this,
            detailViewModel = detailViewModel,
            onBackClick = onBackClick,
            onProfileDetailClick = { username ->
                onProfileDetailClick.invoke(username)
            },
            onTagClick = { tag ->
                onTagClick.invoke(tag)
            }
        )
    }
}