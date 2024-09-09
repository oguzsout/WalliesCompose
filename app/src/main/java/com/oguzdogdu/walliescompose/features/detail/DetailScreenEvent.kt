package com.oguzdogdu.walliescompose.features.detail

import com.oguzdogdu.walliescompose.domain.model.favorites.FavoriteImages

sealed class DetailScreenEvent {
    data object GetPhotoDetails : DetailScreenEvent()
    data object GetFavoriteCheckStat : DetailScreenEvent()
    data object GetFavoriteListForQuickInfo : DetailScreenEvent()
    data class PhotoQualityType(val type:TypeOfPhotoQuality) : DetailScreenEvent()
    data class SetWallpaperPlace(val type: TypeOfSetWallpaper) : DetailScreenEvent()
    data class OpenDownloadBottomSheet(val isOpen: Boolean) : DetailScreenEvent()
    data class OpenSetWallpaperBottomSheet(val isOpen: Boolean) : DetailScreenEvent()
    data class AddFavorites(val favorites: FavoriteImages) : DetailScreenEvent()
    data class DeleteFavorites(val favorites: FavoriteImages) : DetailScreenEvent()
}
