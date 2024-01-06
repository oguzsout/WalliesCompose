package com.oguzdogdu.walliescompose.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.walliescompose.domain.repository.AppSettingsRepository
import com.oguzdogdu.walliescompose.features.settings.SettingsScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStore: AppSettingsRepository,
) : ViewModel() {
    private val _mainState = MutableStateFlow(MainScreenState())
    val mainState = _mainState.asStateFlow()

    init {
        getThemeValue()
    }

    fun handleScreenEvents(event: SettingsScreenEvent) {
        when (event) {
            is SettingsScreenEvent.ThemeChanged -> getThemeValue()
            else -> {}
        }
    }

    fun getThemeValue() {
        viewModelScope.launch {
            dataStore.getThemeStrings(key = "theme").collectLatest { value ->
                _mainState.update { it.copy(getThemeValue = value) }
            }
        }
    }
}