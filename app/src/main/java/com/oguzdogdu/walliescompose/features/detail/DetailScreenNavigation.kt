package com.oguzdogdu.walliescompose.features.detail

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.oguzdogdu.walliescompose.util.adjustUrlForScreenConstraints
import com.oguzdogdu.walliescompose.util.downloadImageFromWeb
import com.oguzdogdu.walliescompose.util.setWallpaperFromUrl


const val DetailScreenNavigationRoute = "detail_screen_route"

fun NavController.navigateToDetailScreen(
    photoId: String?,
    navOptions: NavOptions? = null,
) {
    this.navigate("$DetailScreenNavigationRoute/$photoId", navOptions)
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.detailScreen(
    transitionScope: SharedTransitionScope,
    onBackClick: () -> Unit,
    onTagClick: (String) -> Unit,
    onProfileDetailClick: (String) -> Unit
) {
    composable(
        "$DetailScreenNavigationRoute/{photoId}",
        arguments = listOf(
            navArgument("photoId") {
                type = NavType.StringType
            }
        )
    ) {
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