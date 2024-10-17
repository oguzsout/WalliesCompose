package com.oguzdogdu.walliescompose.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.walliescompose.WalliesApplication
import com.oguzdogdu.walliescompose.data.common.takeListOr
import com.oguzdogdu.walliescompose.data.repository.AppSettingsRepositoryImpl.Companion.HOME_IMAGE_ROTATE_KEY
import com.oguzdogdu.walliescompose.domain.model.popular.PopularImage
import com.oguzdogdu.walliescompose.domain.model.random.RandomImage
import com.oguzdogdu.walliescompose.domain.model.topics.Topics
import com.oguzdogdu.walliescompose.domain.repository.AppSettingsRepository
import com.oguzdogdu.walliescompose.domain.repository.UserAuthenticationRepository
import com.oguzdogdu.walliescompose.domain.repository.WallpaperRepository
import com.oguzdogdu.walliescompose.domain.wrapper.Resource
import com.oguzdogdu.walliescompose.domain.wrapper.onFailure
import com.oguzdogdu.walliescompose.domain.wrapper.onLoading
import com.oguzdogdu.walliescompose.domain.wrapper.onSuccess
import com.oguzdogdu.walliescompose.features.home.event.HomeScreenEvent
import com.oguzdogdu.walliescompose.features.home.state.HomeUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WallpaperRepository,
    private val authenticationRepository: UserAuthenticationRepository,
    private val application: WalliesApplication,
    private val appSettingsRepository: AppSettingsRepository
) : ViewModel() {

    private val _homeListState = MutableStateFlow<HomeUIState>(HomeUIState.Loading())
    val homeListState = _homeListState.asStateFlow()

    private val _userProfileImage = MutableStateFlow<String?>("")
    val userProfileImage = _userProfileImage.asStateFlow()

    val homeRotateCardVisibilty: StateFlow<Boolean> =
        appSettingsRepository.getHomeRotateCardVisibility(HOME_IMAGE_ROTATE_KEY)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = true
            )

    fun handleScreenEvents(event: HomeScreenEvent) {
        when (event) {
            HomeScreenEvent.FetchHomeScreenLists -> fetchHomeScreenData()
            HomeScreenEvent.FetchMainScreenUserData -> checkUserAuthState()
        }
    }

    private fun fetchHomeScreenData() {
        viewModelScope.launch {
            combineResults(
                repository.getRandomImages(count = 8),
                repository.getHomeTopicsImages(),
                repository.getHomeImagesByPopulars(),
            ).collect { homeUIState ->
                updateHomeListState(homeUIState)
            }
        }
    }

    private fun combineResults(
        randomResult: Flow<Resource<List<RandomImage>?>>,
        topicsResult: Flow<Resource<List<Topics>?>>,
        popularsResult: Flow<Resource<List<PopularImage>?>>
    ): Flow<HomeUIState> {
        return combine(
            randomResult,
            topicsResult,
            popularsResult
        ) { randomResource, topicsResource, popularsResource ->
            val isLoading = randomResource is Resource.Loading ||
                    topicsResource is Resource.Loading ||
                    popularsResource is Resource.Loading

            if (isLoading) {
                return@combine HomeUIState.Loading(loading = true)
            }

            val randomList = randomResource.takeListOr { emptyList() }
            val topicsList = topicsResource.takeListOr { emptyList() }
            val popularList = popularsResource.takeListOr { emptyList() }

            val combinedData = HomeUIState.Success(
                random = randomList,
                topics = topicsList,
                popular = popularList,
            )

            getImageListForGlanceList(popularList)

            if (combinedData.isEmpty()) {
                HomeUIState.Error("No data found")
            } else {
                combinedData
            }
        }
    }

    private fun updateHomeListState(homeUIState: HomeUIState) {
        _homeListState.update { homeUIState }
    }

    private fun getImageListForGlanceList(popularList: List<PopularImage>) {
        viewModelScope.launch {
            application.imagesList.clear()
            popularList.take(10).forEach { item ->
                application.imagesList.add(item.url)
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
    fun setHomeCardInfo(visibilty: Boolean) = viewModelScope.launch {
        appSettingsRepository.putHomeRotateCardVisibility(
            key = HOME_IMAGE_ROTATE_KEY,
            value = visibilty
        )
    }
}