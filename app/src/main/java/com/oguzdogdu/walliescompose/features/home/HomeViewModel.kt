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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
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
                fetchHomeScreenData()
            }
        }
    }
    private fun fetchHomeScreenData() {
        viewModelScope.launch {
            combine(
                repository.getHomeTopicsImages(),
                repository.getHomeImagesByPopulars(),
                repository.getHomeImagesByLatest()
            ) { topicsResult, popularsResult, latestResult ->

                _homeListState.update { it.copy(loading = true) }

                topicsResult.onSuccess { topics ->
                    _homeListState.update { it.copy(loading = false, topics = topics.orEmpty()) }
                }
                popularsResult.onSuccess { populars ->
                    _homeListState.update { it.copy(loading = false, popular = populars.orEmpty()) }
                }
                latestResult.onSuccess { latest ->
                    _homeListState.update { it.copy(loading = false, latest = latest.orEmpty()) }
                }

                topicsResult.onFailure { error ->
                    _homeListState.update { it.copy(loading = false, error = error) }
                }
                popularsResult.onFailure { error ->
                    _homeListState.update { it.copy(loading = false, error = error) }
                }
                latestResult.onFailure { error ->
                    _homeListState.update { it.copy(loading = false, error = error) }
                }
            }.collect()
        }
    }
}