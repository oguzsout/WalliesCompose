package com.oguzdogdu.walliescompose.features.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.oguzdogdu.walliescompose.domain.model.favorites.FavoriteImages
import com.oguzdogdu.walliescompose.domain.repository.UserAuthenticationRepository
import com.oguzdogdu.walliescompose.domain.repository.WallpaperRepository
import com.oguzdogdu.walliescompose.domain.wrapper.onFailure
import com.oguzdogdu.walliescompose.domain.wrapper.onLoading
import com.oguzdogdu.walliescompose.domain.wrapper.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel@Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: WallpaperRepository,
    private val userAuthenticationRepository: UserAuthenticationRepository
) : ViewModel() {
    private val _getPhoto = MutableStateFlow(DetailState())
    val photo = _getPhoto.asStateFlow()

    private val _downloadBottomSheetOpenStat = MutableStateFlow(false)
    val downloadBottomSheetOpenStat = _downloadBottomSheetOpenStat.asStateFlow()

    private val _setWallpaperBottomSheetOpenStat = MutableStateFlow(false)
    val setWallpaperBottomSheetOpenStat = _setWallpaperBottomSheetOpenStat.asStateFlow()

    private val _photoQualityType = MutableStateFlow("")
    val photoQualityType = _photoQualityType.asStateFlow()

    private val _setWallpaperPlace = MutableStateFlow("")
    val setWallpaperPlace = _setWallpaperPlace.asStateFlow()

     val id: String = checkNotNull(savedStateHandle.toRoute<DetailScreenRoute>().photoId)

    fun handleScreenEvents(event: DetailScreenEvent) {
        when (event) {
            is DetailScreenEvent.GetPhotoDetails -> {
                getSinglePhoto()
            }

            is DetailScreenEvent.AddFavorites -> addOrDeleteFavoritesToAnyDatabase(
              favoriteImage = event.favorites, process = DatabaseProcess.ADD.name
            )

            is DetailScreenEvent.DeleteFavorites -> addOrDeleteFavoritesToAnyDatabase(
                favoriteImage = event.favorites, process = DatabaseProcess.DELETE.name
            )

            is DetailScreenEvent.GetFavoriteCheckStat -> getFavoritesForCheckFromRoom(id)
            is DetailScreenEvent.OpenDownloadBottomSheet -> {
                _downloadBottomSheetOpenStat.value = event.isOpen
            }

            is DetailScreenEvent.PhotoQualityType ->  {
                _photoQualityType.value = event.type.name
            }

            is DetailScreenEvent.OpenSetWallpaperBottomSheet -> {
                _setWallpaperBottomSheetOpenStat.value = event.isOpen
            }

            is DetailScreenEvent.SetWallpaperPlace -> {
                _setWallpaperPlace.value = event.type.name
            }

            DetailScreenEvent.GetFavoriteListForQuickInfo -> fetchFavoriteImageListForQuickInfo()
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

    fun setNullValueOfImageUrl() {
        viewModelScope.launch {
            _photoQualityType.value = ""
        }
    }

    private fun addOrDeleteFavoritesToAnyDatabase(favoriteImage: FavoriteImages, process: String?) {
        viewModelScope.launch {
            userAuthenticationRepository.isUserAuthenticatedInFirebase().collectLatest { status ->
                when (status) {
                    true -> {
                        when (process) {
                            DatabaseProcess.ADD.name -> addImagesToFavorites(
                                favoriteImage,
                                storage = ChooseDB.FIREBASE.name
                            )

                            DatabaseProcess.DELETE.name -> deleteImageFromFavorites(
                                favoriteImage,
                                storage = ChooseDB.FIREBASE.name
                            )
                        }
                    }

                    false -> {
                        when (process) {
                            DatabaseProcess.ADD.name -> {
                                addImagesToFavorites(
                                    favoriteImage,
                                    storage = ChooseDB.ROOM.name
                                )
                            }

                            DatabaseProcess.DELETE.name -> deleteImageFromFavorites(
                                favoriteImage,
                                storage = ChooseDB.ROOM.name
                            )
                        }
                    }
                }
            }
        }
    }

    private fun addImagesToFavorites(
        favoriteImage: FavoriteImages,
        storage: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        when (storage) {
            ChooseDB.FIREBASE.name -> {
                userAuthenticationRepository.addFavorites(
                    id = favoriteImage.id,
                    favorite = favoriteImage.url
                )
            }

            ChooseDB.ROOM.name -> {
                repository.insertImageToFavorites(favoriteImage)
            }
        }
    }

    private fun deleteImageFromFavorites(
        favoriteImage: FavoriteImages,
        storage: String
    ) = viewModelScope.launch {
        when(storage) {
            ChooseDB.FIREBASE.name -> {
                userAuthenticationRepository.deleteFavorites(
                    id = favoriteImage.id,
                    favorite = favoriteImage.url
                )
            }
            ChooseDB.ROOM.name -> {
                repository.deleteFavorites(favoriteImage)
            }
        }
    }

    private fun getFavoritesForCheckFromRoom(id: String?) {
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
    private fun fetchFavoriteImageListForQuickInfo() {
        viewModelScope.launch {
            repository.getFavorites().collect { result ->
                result.onSuccess { list ->
                    _getPhoto.update {
                        it.copy(favoriteImagesList = list.orEmpty())
                    }
                }
            }
        }
    }
}

enum class TypeOfPhotoQuality {
    LOW,
    MEDIUM,
    HIGH,
    RAW
}
enum class TypeOfSetWallpaper {
    LOCK,
    HOME,
    HOME_AND_LOCK
}
enum class ChooseDB(name: String) {
    FIREBASE("firebase"),
    ROOM("room")
}
enum class DatabaseProcess(name: String) {
    ADD("add"),
    DELETE("delete")
}