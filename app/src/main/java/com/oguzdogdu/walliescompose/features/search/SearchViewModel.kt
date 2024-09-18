package com.oguzdogdu.walliescompose.features.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.oguzdogdu.walliescompose.domain.model.search.SearchPhoto
import com.oguzdogdu.walliescompose.domain.model.search.searchuser.SearchUser
import com.oguzdogdu.walliescompose.domain.model.userpreferences.UserPreferences
import com.oguzdogdu.walliescompose.domain.repository.AppSettingsRepository
import com.oguzdogdu.walliescompose.domain.repository.UnsplashUserRepository
import com.oguzdogdu.walliescompose.domain.repository.WallpaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val wallpaperRepository: WallpaperRepository,
    private val unsplashUserRepository: UnsplashUserRepository,
    private val appRepository: AppSettingsRepository
) : ViewModel() {
    private val _searchListState: MutableStateFlow<PagingData<SearchPhoto>> =
        MutableStateFlow(value = PagingData.empty())
    val searchListState: MutableStateFlow<PagingData<SearchPhoto>> get() = _searchListState

    private val _searchUserListState: MutableStateFlow<PagingData<SearchUser>> =
        MutableStateFlow(value = PagingData.empty())
    val searchUserListState= _searchUserListState.asStateFlow()

    private val _searchScreenState = MutableStateFlow(SearchState())
    val searchScreenState = _searchScreenState.asStateFlow()

    var query = MutableStateFlow("")

    var appLanguage = MutableStateFlow("")
        private set

    fun handleUIEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.EnteredSearchQuery -> {
                asyncQuery(query = event.query, language = event.language)
                query.value = event.query.toString()
                addSearchKeysToDB(userPreferences = UserPreferences(keyword = event.query))
            }

            is SearchEvent.GetAppLanguageValue -> getLanguageValue()
            is SearchEvent.OpenSpeechDialog -> { stateOfSpeechDialog(event.isOpen) }
            is SearchEvent.DeleteFromUserPreferences -> deleteKeywordIfExists(event.keyword)
            is SearchEvent.GetRecentKeywords -> getSearchKeysFromDB()
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun asyncQuery(query: String?, language: String?) {
        viewModelScope.launch {
            getSearchPhotos(query = query, language = language).flatMapConcat { photos ->
                _searchListState.value = photos
                getSearchUsers(query = query)
            }.collect { users ->
                _searchUserListState.value = users
            }
        }
    }

    private fun addSearchKeysToDB(userPreferences: UserPreferences) {
        viewModelScope.launch {
            getSearchKeysFromDB()
                val isKeywordExists = _searchScreenState.value.userPreferences.contains(userPreferences)
                if (!isKeywordExists) {
                    this.launch {
                        wallpaperRepository.insertRecentSearchKeysToDB(userPreferences = userPreferences)
                }
            }
        }
    }

    private fun getSearchKeysFromDB() {
        viewModelScope.launch {
            wallpaperRepository.getRecentSearchKeysFromDB().collect { list ->
                _searchScreenState.update {
                    it.copy(userPreferences = list.orEmpty())
                }
            }
        }
    }

    private fun deleteKeywordIfExists(recentKeyword: String?) {
        viewModelScope.launch {
            val keyword = recentKeyword ?: return@launch
            val itemToDelete = findKeywordInDatabase(keyword)
            itemToDelete?.let { deleteKeywordFromDatabase(it.keyword.orEmpty()) }
        }
    }

    private suspend fun findKeywordInDatabase(keyword: String): UserPreferences? {
        val preferencesList = wallpaperRepository.getRecentSearchKeysFromDB().firstOrNull()
        return preferencesList?.firstOrNull { it.keyword == keyword }
    }

    private suspend fun deleteKeywordFromDatabase(keyword: String) {
        wallpaperRepository.deleteRecentSearchKeysFromDB(keyword)
    }


    private fun stateOfSpeechDialog(isOpen:Boolean) {
        viewModelScope.launch {
           _searchScreenState.update {
               it.copy(speechDialogState = isOpen)
           }
        }
    }

    private suspend fun getSearchPhotos(
        query: String?,
        language: String?
    ): Flow<PagingData<SearchPhoto>> {
        return wallpaperRepository.searchPhoto(query, language).cachedIn(viewModelScope)
    }

    private suspend fun getSearchUsers(query: String?): Flow<PagingData<SearchUser>> {
        return unsplashUserRepository.getSearchFromUsers(query = query).cachedIn(viewModelScope)
    }
    private fun getLanguageValue() {
        viewModelScope.launch {
            val language = appRepository.getLanguageStrings(key = "language").single()
            appLanguage.value = language ?: "en"
        }
    }
}