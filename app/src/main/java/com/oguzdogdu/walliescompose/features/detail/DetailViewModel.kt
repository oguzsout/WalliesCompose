package com.oguzdogdu.walliescompose.features.detail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.oguzdogdu.walliescompose.domain.repository.WallpaperRepository
import com.oguzdogdu.walliescompose.features.home.event.HomeScreenEvent
import com.oguzdogdu.walliescompose.features.home.state.HomeScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DetailViewModel@Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: WallpaperRepository
) : ViewModel() {
    private val _homeListState = MutableStateFlow(HomeScreenState())
    val homeListState = _homeListState.asStateFlow()

     val id: String = checkNotNull(savedStateHandle["photoId"])

    // Fetch the relevant user information from the data layer,
    // ie. userInfoRepository, based on the passed userId argument
     var userInfo = mutableStateOf(id)

    fun handleScreenEvents(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.FetchHomeScreenLists -> {

            }
        }
    }

}