package com.oguzdogdu.walliescompose.features.detail

sealed class DetailScreenEvent {
    data object GetPhotoDetails : DetailScreenEvent()
    data object GetFavoriteCheckStat : DetailScreenEvent()
    data object GetFavoriteListForQuickInfo : DetailScreenEvent()
    data object AddFavorites : DetailScreenEvent()
    data object DeleteFavorites : DetailScreenEvent()
    data class PhotoQualityType(val type:TypeOfPhotoQuality) : DetailScreenEvent()
    data class SetWallpaperPlace(val type: TypeOfSetWallpaper) : DetailScreenEvent()
    data class OpenDownloadBottomSheet(val isOpen: Boolean) : DetailScreenEvent()
    data class OpenSetWallpaperBottomSheet(val isOpen: Boolean) : DetailScreenEvent()
}
