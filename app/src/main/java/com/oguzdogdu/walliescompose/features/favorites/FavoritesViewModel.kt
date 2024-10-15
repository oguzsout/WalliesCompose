package com.oguzdogdu.walliescompose.features.favorites

import androidx.lifecycle.viewModelScope
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.core.BaseViewModel
import com.oguzdogdu.walliescompose.domain.repository.WallpaperRepository
import com.oguzdogdu.walliescompose.features.appstate.Duration
import com.oguzdogdu.walliescompose.features.appstate.MessageContent
import com.oguzdogdu.walliescompose.features.appstate.MessageType
import com.oguzdogdu.walliescompose.features.appstate.SnackbarModel
import com.oguzdogdu.walliescompose.features.favorites.effect.FavoriteScreenEffect
import com.oguzdogdu.walliescompose.features.favorites.event.FavoriteScreenEvent
import com.oguzdogdu.walliescompose.features.favorites.state.FavoriteScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val repository: WallpaperRepository):
    BaseViewModel<FavoriteScreenState, FavoriteScreenEvent, FavoriteScreenEffect>(initialState = FavoriteScreenState()) {

    override fun handleEvents(event: FavoriteScreenEvent) {
        when (event) {
            is FavoriteScreenEvent.GetFavorites -> fetchImagesToFavorites()
            is FavoriteScreenEvent.FlipToImage -> setFlipCardState(event.flip)
            is FavoriteScreenEvent.DeleteFromFavorites -> deleteWithIdFromFavorites(event.favoriteId)
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
                    message = MessageContent.PlainString(error.message.orEmpty()),
                    duration = Duration.SHORT
                )))
            },
            onComplete = {}
        )
    }

    private fun deleteWithIdFromFavorites(favoriteId: String) {
        viewModelScope.launch {
            async { repository.deleteSpecificIdFavorite(favoriteId) }.await()
            repository.getFavorites().collect { favorites ->
                val isDeleteSuccessful = favorites?.none { it.id == favoriteId } ?: true
                if (isDeleteSuccessful) {
                    sendEffect(FavoriteScreenEffect.ShowSnackbar(SnackbarModel(
                        type = MessageType.SUCCESS,
                        drawableRes = R.drawable.ic_completed,
                        message = MessageContent.ResourceString(R.string.delete_favorites),
                        duration = Duration.SHORT
                    )))
                }
            }
        }
    }

      fun setFlipCardState(state:Boolean) {
        viewModelScope.launch {
            setState(currentState.copy(flipToCard = state))
        }
    }
}