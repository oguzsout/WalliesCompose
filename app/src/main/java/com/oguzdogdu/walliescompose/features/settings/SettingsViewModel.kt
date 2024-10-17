package com.oguzdogdu.walliescompose.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.walliescompose.WalliesApplication
import com.oguzdogdu.walliescompose.data.repository.AppSettingsRepositoryImpl.Companion.LANGUAGE_KEY
import com.oguzdogdu.walliescompose.data.repository.AppSettingsRepositoryImpl.Companion.THEME_KEY
import com.oguzdogdu.walliescompose.domain.repository.AppSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: AppSettingsRepository
    , private val application: WalliesApplication
) : ViewModel() {

    private val _settingsState = MutableStateFlow(SettingsScreenState())
    val settingsState = _settingsState.asStateFlow()

    fun handleScreenEvents(event: SettingsScreenEvent) {
        when (event) {
            is SettingsScreenEvent.OpenThemeDialog -> { openTheme(event.open) }
            is SettingsScreenEvent.SetNewTheme -> setThemeValue(event.value)
            is SettingsScreenEvent.ThemeChanged -> { getThemeValue() }
            is SettingsScreenEvent.LanguageChanged -> getLanguageValue()
            is SettingsScreenEvent.SetNewLanguage -> setLanguageValue(event.value)
            is SettingsScreenEvent.OpenLanguageDialog -> openLanguage(event.open)
            is SettingsScreenEvent.ClearCached -> statusClearCache(event.isCleared)
        }
    }

    private fun statusClearCache(showSnackBar:Boolean?) {
        viewModelScope.launch {
            _settingsState.update {
                it.copy(showSnackBar = showSnackBar)
            }
        }
    }

    private fun openTheme(open: Boolean) {
        viewModelScope.launch {
            _settingsState.update { it.copy(openThemeDialog = open) }
        }
    }

    private fun openLanguage(open: Boolean) {
        viewModelScope.launch {
            _settingsState.update { it.copy(openLanguageDialog = open) }
        }
    }

    private fun setThemeValue(value: String) = runBlocking {
        dataStore.putThemeStrings(key = THEME_KEY, value = value)
    }

    private fun getThemeValue() {
        viewModelScope.launch {
            dataStore.getThemeStrings(key = THEME_KEY).collect { value ->
                _settingsState.updateAndGet {
                    it.copy(getThemeValue = value)
                }
                if (value != null) {
                    application.theme.value = value
                }
            }
        }
    }

    private fun setLanguageValue(value: String) = runBlocking {
        dataStore.putLanguageStrings(key = LANGUAGE_KEY, value = value)
    }

    private fun getLanguageValue() {
        viewModelScope.launch {
            dataStore.getLanguageStrings(key = LANGUAGE_KEY).collect { value ->
                _settingsState.update {
                    it.copy(getLanguageValue = value)
                }
                if (value != null) {
                    application.language.value = value
                }
            }
        }
    }
}