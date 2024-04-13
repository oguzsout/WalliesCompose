package com.oguzdogdu.walliescompose.features.favorites.event

sealed class FavoriteScreenEvent {
    data object GetFavorites : FavoriteScreenEvent()
    data class FlipToImage(val flip:Boolean): FavoriteScreenEvent()
    data class DeleteFromFavorites(val favoriteId:String) : FavoriteScreenEvent()
}
