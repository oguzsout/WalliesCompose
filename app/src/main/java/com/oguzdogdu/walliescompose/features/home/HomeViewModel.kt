package com.oguzdogdu.walliescompose.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.walliescompose.WalliesApplication
import com.oguzdogdu.walliescompose.data.common.collectSuccess
import com.oguzdogdu.walliescompose.domain.model.latest.LatestImage
import com.oguzdogdu.walliescompose.domain.model.popular.PopularImage
import com.oguzdogdu.walliescompose.domain.model.random.RandomImage
import com.oguzdogdu.walliescompose.domain.model.topics.Topics
import com.oguzdogdu.walliescompose.domain.repository.UserAuthenticationRepository
import com.oguzdogdu.walliescompose.domain.repository.WallpaperRepository
import com.oguzdogdu.walliescompose.domain.wrapper.Resource
import com.oguzdogdu.walliescompose.domain.wrapper.onFailure
import com.oguzdogdu.walliescompose.domain.wrapper.onLoading
import com.oguzdogdu.walliescompose.domain.wrapper.onSuccess
import com.oguzdogdu.walliescompose.features.home.event.HomeScreenEvent
import com.oguzdogdu.walliescompose.features.home.state.HomeUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WallpaperRepository,
    private val authenticationRepository: UserAuthenticationRepository,
    private val application: WalliesApplication
) : ViewModel() {

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
            val topicsResult = repository.getHomeTopicsImages()
            val popularsResult = repository.getHomeImagesByPopulars()
            val latestResult = repository.getHomeImagesByLatest()
            val randomResult = repository.getRandomImages(count = 8)

            combineResults(topicsResult, popularsResult, latestResult, randomResult).collect { homeUIState ->
                updateHomeListState(homeUIState)
            }
        }
    }

    private suspend fun combineResults(
        topicsResult: Flow<Resource<List<Topics>?>>,
        popularsResult: Flow<Resource<List<PopularImage>?>>,
        latestResult: Flow<Resource<List<LatestImage>?>>,
        randomResult: Flow<Resource<List<RandomImage>?>>
    ): Flow<HomeUIState> = channelFlow {
        val topicsChannel = Channel<List<Topics>>()
        val popularsChannel = Channel<List<PopularImage>>()
        val latestChannel = Channel<List<LatestImage>>()
        val randomChannel = Channel<List<RandomImage>>()

        viewModelScope.launch {
            launch { topicsResult.collectSuccess { topicsChannel.send(it) } }
            launch { popularsResult.collectSuccess { popularsChannel.send(it) } }
            launch { latestResult.collectSuccess { latestChannel.send(it) } }
            launch { randomResult.collectSuccess { randomChannel.send(it) } }
        }

        val topicsList = topicsChannel.receiveCatching().getOrNull() ?: emptyList()
        val popularList = popularsChannel.receiveCatching().getOrNull() ?: emptyList()
        val latestList = latestChannel.receiveCatching().getOrNull() ?: emptyList()
        val randomList = randomChannel.receiveCatching().getOrNull() ?: emptyList()

        application.imagesList.clear()
        popularList.take(10).forEach { item ->
            application.imagesList.add(item.url)
        }

        val combinedData = HomeUIState.Success(
            topics = topicsList,
            popular = popularList,
            latest = latestList,
            random = randomList
        )

        if (combinedData.isEmpty()) {
            trySend(HomeUIState.Error("No data found"))
        } else {
            trySend(combinedData)
        }
        close()
    }

    private fun updateHomeListState(homeUIState: HomeUIState) {
        _homeListState.update { homeUIState }
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