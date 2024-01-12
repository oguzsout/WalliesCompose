package com.oguzdogdu.walliescompose.features.favorites.event

sealed class FavoriteScreenEvent {
    data object GetFavorites : FavoriteScreenEvent()
    data object DeleteList : FavoriteScreenEvent()
}
