package com.oguzdogdu.walliescompose.features.favorites.event

import com.oguzdogdu.walliescompose.core.ViewEvent

sealed class FavoriteScreenEvent : ViewEvent {
    data object GetFavorites : FavoriteScreenEvent()
    data class FlipToImage(val flip:Boolean): FavoriteScreenEvent()
    data class DeleteFromFavorites(val favoriteId:String) : FavoriteScreenEvent()
}
