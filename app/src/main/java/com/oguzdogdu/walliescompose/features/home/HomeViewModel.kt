package com.oguzdogdu.walliescompose.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.walliescompose.domain.repository.WallpaperRepository
import com.oguzdogdu.walliescompose.domain.wrapper.onFailure
import com.oguzdogdu.walliescompose.domain.wrapper.onLoading
import com.oguzdogdu.walliescompose.domain.wrapper.onSuccess
import com.oguzdogdu.walliescompose.features.home.event.HomeScreenEvent
import com.oguzdogdu.walliescompose.features.home.state.HomeScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WallpaperRepository
) : ViewModel() {
    private val _homeListState = MutableStateFlow(HomeScreenState())
    val homeListState = _homeListState.asStateFlow()

    fun handleScreenEvents(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.FetchHomeScreenLists -> {
                fetchTopicsTitleList()
                fetchHomePopularList()
                fetchHomeLatestList()
            }
        }
    }
    private fun fetchTopicsTitleList() {
        viewModelScope.launch {
            repository.getHomeTopicsImages().collect { result ->
                    result.onLoading {
                        _homeListState.update { it.copy(loading = true) }
                    }
                    result.onSuccess { list ->
                        _homeListState.update { it.copy(loading = false, topics = list.orEmpty()) }
                    }
                    result.onFailure {

                    }
                }
        }
    }

    private fun fetchHomePopularList() {
        viewModelScope.launch {
            repository.getHomeImagesByPopulars().collect { result ->
                    result.onLoading {
                        _homeListState.update { it.copy(loading = true) }
                    }
                    result.onSuccess { list ->
                        _homeListState.update { it.copy(loading = false, popular = list.orEmpty()) }
                    }
                    result.onFailure {

                    }
                }
        }
    }

    private fun fetchHomeLatestList() {
        viewModelScope.launch {
            repository.getHomeImagesByLatest().collect { result ->
                    result.onLoading {
                        _homeListState.update { it.copy(loading = true) }
                    }
                    result.onSuccess { list ->
                        _homeListState.update { it.copy(loading = false, latest = list.orEmpty()) }
                    }
                    result.onFailure {

                    }
                }
        }
    }
}