package com.oguzdogdu.walliescompose.features.profiledetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.oguzdogdu.walliescompose.domain.repository.UnsplashUserRepository
import com.oguzdogdu.walliescompose.domain.wrapper.onFailure
import com.oguzdogdu.walliescompose.domain.wrapper.onSuccess
import com.oguzdogdu.walliescompose.features.profiledetail.event.ProfileDetailEvent
import com.oguzdogdu.walliescompose.features.profiledetail.state.ProfileDetailState
import com.oguzdogdu.walliescompose.features.profiledetail.state.ProfileDetailUIState
import com.oguzdogdu.walliescompose.features.profiledetail.state.UserCollectionState
import com.oguzdogdu.walliescompose.features.profiledetail.state.UserPhotosState
import com.oguzdogdu.walliescompose.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val unsplashUserRepository: UnsplashUserRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val username: String = checkNotNull(savedStateHandle.toRoute<Screens.ProfileDetailScreenNavigationRoute>().username)

    private val _getProfileDetailState = MutableStateFlow<ProfileDetailUIState?>(null)
    val getProfileDetailState = _getProfileDetailState.asStateFlow()

    private val _getUserDetails = MutableStateFlow(ProfileDetailState())
    val getUserDetails = _getUserDetails.asStateFlow()

    private val _getUserPhotoList = MutableStateFlow(UserPhotosState())
    val getUserPhotoList = _getUserPhotoList.asStateFlow()

    private val _getUserCollectionList = MutableStateFlow(UserCollectionState())
    val getUserCollectionList = _getUserCollectionList.asStateFlow()

    fun handleUIEvent(event: ProfileDetailEvent) {
        when (event) {
            is ProfileDetailEvent.FetchUserDetailInfos -> profileDetailScreenState()
        }
    }

    private fun profileDetailScreenState() {
        viewModelScope.launch {
            try {
                _getProfileDetailState.update { ProfileDetailUIState.Loading }
                delay(1000)
                val userDetailsDeferred = async { getUserDetails() }
                val usersPhotosDeferred = async { getUsersPhotos() }
                val usersCollectionsDeferred = async { getUsersCollections() }

                awaitAll(userDetailsDeferred,usersPhotosDeferred,usersCollectionsDeferred)

                if (userDetailsDeferred.isCompleted && usersPhotosDeferred.isCompleted && usersCollectionsDeferred.isCompleted) {
                    _getProfileDetailState.update {
                        ProfileDetailUIState.ReadyForShown
                    }
                }
            } catch (e: Exception) {
                _getProfileDetailState.update {
                    ProfileDetailUIState.Error("An error occurred: ${e.message}")
                }
            }
        }
    }

    private fun getUserDetails() =
        viewModelScope.launch {
            unsplashUserRepository.getUserDetails(username).collectLatest { result ->
                result.onSuccess { userDetails ->
                    _getUserDetails.update {
                        it.copy(loading = false, userDetails = userDetails)
                    }
                }
                result.onFailure { error ->
                    _getUserDetails.update {
                        it.copy(loading = false, errorMessage = error)
                    }
                }
            }
        }


    private fun getUsersPhotos() =
        viewModelScope.launch {
            _getUserPhotoList.update { it.copy(loading = true) }

            unsplashUserRepository.getUsersPhotos(username).collect { result ->
                result.onSuccess { list ->
                    _getUserPhotoList.update { it.copy(loading = false, usersPhotos = list) }
                }

                result.onFailure { error ->
                    _getUserPhotoList.update { it.copy(loading = false, errorMessage = error) }
                }
            }
        }


    private fun getUsersCollections() =
        viewModelScope.launch {
            _getUserCollectionList.update { it.copy(loading = true) }

            unsplashUserRepository.getUsersCollections(username).collect { result ->
                result.onSuccess { list ->
                    _getUserCollectionList.update {
                        it.copy(loading = false, usersCollection = list)
                    }
                }

                result.onFailure { error ->
                    _getUserCollectionList.update { it.copy(loading = false, errorMessage = error) }
                }
            }
        }
    }