package com.oguzdogdu.walliescompose.features.favorites.effect

import com.oguzdogdu.walliescompose.core.ViewEffect
import com.oguzdogdu.walliescompose.features.appstate.SnackbarModel

sealed interface FavoriteScreenEffect : ViewEffect {
    data class ShowSnackbar(val snackbarModel: SnackbarModel) : FavoriteScreenEffect
}
