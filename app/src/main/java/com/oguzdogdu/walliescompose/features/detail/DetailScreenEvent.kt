package com.oguzdogdu.walliescompose.features.detail

import com.oguzdogdu.walliescompose.domain.model.detail.Photo
import com.oguzdogdu.walliescompose.domain.model.favorites.FavoriteImages

sealed class DetailScreenEvent {
    data object GetPhotoDetails : DetailScreenEvent()
    data object GetFavoriteCheckStat : DetailScreenEvent()
    data class AddFavorites(val favorites: FavoriteImages) : DetailScreenEvent()
    data class DeleteFavorites(val favorites: FavoriteImages) : DetailScreenEvent()
}
