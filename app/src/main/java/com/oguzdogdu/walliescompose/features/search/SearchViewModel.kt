package com.oguzdogdu.walliescompose.features.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.oguzdogdu.walliescompose.domain.model.collections.WallpaperCollections
import com.oguzdogdu.walliescompose.domain.model.search.SearchPhoto
import com.oguzdogdu.walliescompose.domain.repository.AppSettingsRepository
import com.oguzdogdu.walliescompose.domain.repository.WallpaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: WallpaperRepository,
    private val appRepository: AppSettingsRepository
) : ViewModel() {
    private val _searchListState: MutableStateFlow<PagingData<SearchPhoto>> =
        MutableStateFlow(value = PagingData.empty())
    val searchListState: MutableStateFlow<PagingData<SearchPhoto>> get() = _searchListState

    var appLanguage = MutableStateFlow("")
        private set

    fun handleUIEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.EnteredSearchQuery -> {
                getSearchPhotos(query = event.query, language = event.language)
            }

            is SearchEvent.GetAppLanguageValue -> getLanguageValue()
        }
    }


    private fun getSearchPhotos(query: String?, language: String?) {
        viewModelScope.launch {
            repository.searchPhoto(query, language).cachedIn(viewModelScope)
                .collect { search ->
                    _searchListState.value = search
                }
        }
    }
    private fun getLanguageValue() {
        viewModelScope.launch {
            val language = appRepository.getLanguageStrings(key = "language").single()
            appLanguage.value = language ?: "en"
        }
    }
}