package com.oguzdogdu.walliescompose.features.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.walliescompose.domain.model.favorites.FavoriteImages
import com.oguzdogdu.walliescompose.domain.repository.WallpaperRepository
import com.oguzdogdu.walliescompose.domain.wrapper.onFailure
import com.oguzdogdu.walliescompose.domain.wrapper.onLoading
import com.oguzdogdu.walliescompose.domain.wrapper.onSuccess
import com.oguzdogdu.walliescompose.features.favorites.event.FavoriteScreenEvent
import com.oguzdogdu.walliescompose.features.favorites.state.FavoriteScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val repository: WallpaperRepository): ViewModel() {

    private val _favoritesState: MutableStateFlow<FavoriteScreenState> = MutableStateFlow(
        FavoriteScreenState()
    )
    val favoritesState: MutableStateFlow<FavoriteScreenState> get() = _favoritesState

    fun handleUIEvent(event: FavoriteScreenEvent) {
        when (event) {
            is FavoriteScreenEvent.GetFavorites -> fetchImagesToFavorites()
            FavoriteScreenEvent.DeleteList -> delete()
        }
    }
    private fun delete() {
        _favoritesState.update { it.copy(favorites = null) }
    }
    private fun fetchImagesToFavorites() {
        viewModelScope.launch {
            repository.getFavorites().collectLatest { status ->
                status.onLoading {
                    _favoritesState.update { it.copy(loading = true) }
                }
                status.onSuccess {list->
                    _favoritesState.update {
                        it.copy(loading = false,favorites = listOf(FavoriteImages(profileImage = "")))
                    }
                }
                status.onFailure { error ->
                    _favoritesState.update { it.copy(error = error) }
                }
            }
        }
    }
}