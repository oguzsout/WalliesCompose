package com.oguzdogdu.walliescompose.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.walliescompose.WalliesApplication
import com.oguzdogdu.walliescompose.domain.repository.AppSettingsRepository
import com.oguzdogdu.walliescompose.domain.repository.UserAuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStore: AppSettingsRepository, private val application: WalliesApplication,
    private val authenticationRepository: UserAuthenticationRepository
) : ViewModel() {
    private val _mainState = MutableStateFlow(MainScreenState())
    val mainState = _mainState.asStateFlow()

    fun handleScreenEvents(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.ThemeChanged -> {
                getThemeValue()
            }

            MainScreenEvent.LanguageChanged -> {
                getLanguageValue()
            }

            MainScreenEvent.CheckUserAuthState -> {
                checkUserAuthState()
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
    private fun getLanguageValue() {
        viewModelScope.launch {
            dataStore.getLanguageStrings(key = "language").collect { value ->
                if (value != null) {
                    application.language.value = value
                }
            }
        }
    }
    private fun checkUserAuthState() {
        viewModelScope.launch {
           val state = authenticationRepository.isUserAuthenticatedInFirebase().single()
            _mainState.update { it.copy(userAuth = state) }
        }
    }
}