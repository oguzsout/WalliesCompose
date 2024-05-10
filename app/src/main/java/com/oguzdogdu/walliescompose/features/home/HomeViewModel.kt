package com.oguzdogdu.walliescompose.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.walliescompose.WalliesApplication
import com.oguzdogdu.walliescompose.domain.model.latest.LatestImage
import com.oguzdogdu.walliescompose.domain.model.popular.PopularImage
import com.oguzdogdu.walliescompose.domain.model.topics.Topics
import com.oguzdogdu.walliescompose.domain.repository.UserAuthenticationRepository
import com.oguzdogdu.walliescompose.domain.repository.WallpaperRepository
import com.oguzdogdu.walliescompose.domain.wrapper.onFailure
import com.oguzdogdu.walliescompose.domain.wrapper.onLoading
import com.oguzdogdu.walliescompose.domain.wrapper.onSuccess
import com.oguzdogdu.walliescompose.features.home.event.HomeScreenEvent
import com.oguzdogdu.walliescompose.features.home.state.HomeScreenState
import com.oguzdogdu.walliescompose.features.home.state.HomeUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WallpaperRepository,
    private val authenticationRepository: UserAuthenticationRepository,
    private val application: WalliesApplication
) : ViewModel() {

    private var topicsList: List<Topics> = emptyList()
    private var popularList: List<PopularImage> = emptyList()
    private var latestList: List<LatestImage> = emptyList()
    private val _homeListState = MutableStateFlow<HomeUIState>(HomeUIState.Loading())
    val homeListState = _homeListState.asStateFlow()

    private val _userProfileImage = MutableStateFlow<String?>("")
    val userProfileImage = _userProfileImage.asStateFlow()

    fun handleScreenEvents(event: HomeScreenEvent) {
        when (event) {
             HomeScreenEvent.FetchHomeScreenLists -> {
                fetchHomeScreenData()
            }

            HomeScreenEvent.FetchMainScreenUserData -> {
               checkUserAuthState()
           }
        }
    }
    private fun fetchHomeScreenData() {
        viewModelScope.launch {
            val results = combine(
                repository.getHomeTopicsImages(),
                repository.getHomeImagesByPopulars(),
                repository.getHomeImagesByLatest()
            ) { topics, populars, latest ->

                topics.onSuccess {
                    topicsList = it.orEmpty()
                }
                populars.onSuccess {
                    popularList = it.orEmpty()
                }
                latest.onSuccess {
                    latestList = it.orEmpty()
                }
                Triple(topicsList, popularList, latestList)
            }

            results.collect { (topics, populars, latest) ->
                val combinedData = HomeUIState.Success(
                    topics = topics,
                    popular = populars,
                    latest = latest
                )
                populars.take(10).map {
                    application.imagesList.add(it.url)
                }

                if (combinedData.isEmpty()) {
                    _homeListState.update { HomeUIState.Error("No data found") }
                } else {
                    _homeListState.update { combinedData }
                }
            }
        }
    }

    private fun checkUserAuthState() {
        viewModelScope.launch {
            combine(
                authenticationRepository.isUserAuthenticatedInFirebase(),
                authenticationRepository.isUserAuthenticatedWithGoogle()
            ) { isFirebaseAuthenticated , isGoogleAuthenticated->
                if (isFirebaseAuthenticated || isGoogleAuthenticated){
                    getUserProfileImage()
                }
            }.collect()
        }
    }
    private fun getUserProfileImage() {
        viewModelScope.launch {
            authenticationRepository.fetchUserInfos().collectLatest { value ->
                value.onLoading {}
                value.onSuccess { user ->
                    user?.let {
                        _userProfileImage.value = user.image.orEmpty()
                    }
                }
                value.onFailure {}
            }
        }
    }
}