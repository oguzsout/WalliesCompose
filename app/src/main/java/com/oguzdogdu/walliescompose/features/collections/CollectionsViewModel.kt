package com.oguzdogdu.walliescompose.features.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.oguzdogdu.walliescompose.domain.model.collections.WallpaperCollections
import com.oguzdogdu.walliescompose.domain.repository.WallpaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionsViewModel @Inject constructor(private val repository: WallpaperRepository) :
    ViewModel() {

    private val _getCollections = MutableStateFlow(CollectionState())
    val getCollections = _getCollections.asStateFlow()

    private val _moviesState: MutableStateFlow<PagingData<WallpaperCollections>> = MutableStateFlow(value = PagingData.empty())
    val moviesState: MutableStateFlow<PagingData<WallpaperCollections>> get() = _moviesState

    fun handleUIEvent(event: CollectionScreenEvent) {
        when (event) {
            is CollectionScreenEvent.FetchLatestData -> {
                getCollectionsList()
            }

            is CollectionScreenEvent.SortByTitles -> sortListByTitle()

            is CollectionScreenEvent.SortByLikes -> sortListByLikes()
        }
    }

    private fun getCollectionsList() {
        viewModelScope.launch {
            repository.getCollectionsList().cachedIn(viewModelScope)
                .collectLatest { value: PagingData<WallpaperCollections> ->
                    value.let { list ->
                        _getCollections.update { it.copy(collections = list) }
                        _moviesState.value = list
                    }
                }
        }
    }


    private fun sortListByTitle() {
        viewModelScope.launch {
            repository.getCollectionsList().cachedIn(viewModelScope).collectLatest { sortedPagingData ->
                sortedPagingData.let { list ->
                    _getCollections.update { it.copy(sortedByTitlePagingData = list) }
                }
            }
        }
    }

    private fun sortListByLikes() {
        viewModelScope.launch {
            repository.getCollectionsList().cachedIn(viewModelScope).collectLatest { sortedPagingData ->
                sortedPagingData.let { list ->
                    _getCollections.update { it.copy(sortedByLikesPagingData = list) }
                }
            }
        }
    }
}