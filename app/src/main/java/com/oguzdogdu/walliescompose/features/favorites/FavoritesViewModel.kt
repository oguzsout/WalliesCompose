package com.oguzdogdu.walliescompose.features.favorites

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(): ViewModel() {
    var showEmptyScreen by mutableStateOf(true)
    private val _moviesState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val moviesState: MutableStateFlow<Boolean> get() = _moviesState

    fun check() = moviesState.value
    fun edit(value:Boolean) {
        _moviesState.value = value
    }
}