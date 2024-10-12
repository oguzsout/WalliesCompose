package com.oguzdogdu.walliescompose.features.favorites

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.core.BaseViewModel
import com.oguzdogdu.walliescompose.domain.repository.WallpaperRepository
import com.oguzdogdu.walliescompose.features.appstate.MessageType
import com.oguzdogdu.walliescompose.features.appstate.SnackbarModel
import com.oguzdogdu.walliescompose.features.favorites.effect.FavoriteScreenEffect
import com.oguzdogdu.walliescompose.features.favorites.event.FavoriteScreenEvent
import com.oguzdogdu.walliescompose.features.favorites.state.FavoriteScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val repository: WallpaperRepository):
    BaseViewModel<FavoriteScreenState, FavoriteScreenEvent, FavoriteScreenEffect>(initialState = FavoriteScreenState()) {

    override fun handleEvents(event: FavoriteScreenEvent) {
        when (event) {
            is FavoriteScreenEvent.GetFavorites -> fetchImagesToFavorites()
            is FavoriteScreenEvent.FlipToImage -> setFlipCardState(event.flip)
            is FavoriteScreenEvent.DeleteFromFavorites -> deleteWithIdFromToFavorites(event.favoriteId)
        }
    }

    private fun fetchImagesToFavorites()  {
        sendApiCall(
            request = { repository.getFavorites() },
            onLoading = { loading -> setState(currentState.copy(loading = loading)) },
            onSuccess = { list-> setState(currentState.copy(favorites = list)) },
            onError = { error ->
                sendEffect(FavoriteScreenEffect.ShowSnackbar(SnackbarModel(
                    type = MessageType.ERROR,
                    drawableRes = R.drawable.ic_cancel,
                    message = error.message,
                    duration = SnackbarDuration.Long
                )))
            },
            onComplete = {}
        )
    }

    private fun deleteWithIdFromToFavorites(favoriteId:String) {
        viewModelScope.launch {
            repository.deleteSpecificIdFavorite(favoriteId)
        }
    }

      fun setFlipCardState(state:Boolean) {
        viewModelScope.launch {
            setState(currentState.copy(flipToCard = state))
        }
    }
}