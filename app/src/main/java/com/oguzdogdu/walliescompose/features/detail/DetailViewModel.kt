package com.oguzdogdu.walliescompose.features.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.walliescompose.domain.model.favorites.FavoriteImages
import com.oguzdogdu.walliescompose.domain.repository.WallpaperRepository
import com.oguzdogdu.walliescompose.domain.wrapper.onFailure
import com.oguzdogdu.walliescompose.domain.wrapper.onLoading
import com.oguzdogdu.walliescompose.domain.wrapper.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel@Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: WallpaperRepository
) : ViewModel() {
    private val _getPhoto = MutableStateFlow(DetailState())
    val photo = _getPhoto.asStateFlow()

     val id: String = checkNotNull(savedStateHandle["photoId"])

    fun handleScreenEvents(event: DetailScreenEvent) {
        when (event) {
            is DetailScreenEvent.GetPhotoDetails -> {
                getSinglePhoto()
            }

            is DetailScreenEvent.AddFavorites -> addImagesToFavorites(
              event.favorites
            )

            is DetailScreenEvent.DeleteFavorites -> deleteImageFromFavorites(
              event.favorites
            )

            is DetailScreenEvent.GetFavoriteCheckStat -> getFavoritesFromRoom(id)
        }
    }

    private fun getSinglePhoto() {
        viewModelScope.launch {
            repository.getPhoto(id = id).collectLatest { result ->
                result.onLoading {
                    _getPhoto.updateAndGet { it.copy(loading = true) }
                }

                result.onSuccess { photo ->
                    _getPhoto.updateAndGet { it.copy(loading = false,detail = photo)}
                }

                result.onFailure { error ->
                    _getPhoto.updateAndGet {
                        it.copy(errorMessage = error)
                    }
                }
            }
        }
    }

    private fun addImagesToFavorites(
        favoriteImage: FavoriteImages,
        ) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertImageToFavorites(favoriteImage)
    }

    private fun deleteImageFromFavorites(
        favoriteImage: FavoriteImages
    ) = viewModelScope.launch {
        repository.deleteFavorites(favoriteImage)
    }

    private fun getFavoritesFromRoom(id: String?) {
        viewModelScope.launch {
            repository.getFavorites().collectLatest { result ->
                result.onSuccess { list ->
                    val matchingFavorite = list?.find { it.id == id }
                    _getPhoto.updateAndGet {
                        it.copy(favorites = matchingFavorite)
                    }
                }
            }
        }
    }
}