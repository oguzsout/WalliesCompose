package com.oguzdogdu.walliescompose.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.walliescompose.WalliesApplication
import com.oguzdogdu.walliescompose.domain.repository.AppSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStore: AppSettingsRepository, private val application: WalliesApplication
) : ViewModel() {
    private val _mainState = MutableStateFlow(MainScreenState())
    val mainState = _mainState.asStateFlow()

    fun handleScreenEvents(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.ThemeChanged -> {
                getThemeValue()
            }
        }
    }

     private fun getThemeValue() {
        viewModelScope.launch {
            val value = dataStore.getThemeStrings(key = "theme").first()
            _mainState.update {
                it.copy(themeValues = value)
            }
            application.theme.value = value.toString()
        }
    }
}