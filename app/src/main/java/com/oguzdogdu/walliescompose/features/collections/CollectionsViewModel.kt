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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionsViewModel @Inject constructor(private val repository: WallpaperRepository) :
    ViewModel() {

    private val _collectionPhotosState: MutableStateFlow<PagingData<WallpaperCollections>> = MutableStateFlow(value = PagingData.empty())
    val collectionPhotosState: MutableStateFlow<PagingData<WallpaperCollections>> get() = _collectionPhotosState

    fun handleUIEvent(event: CollectionScreenEvent) {
        when (event) {
            is CollectionScreenEvent.FetchLatestData -> {
                getCollectionsList()
            }

            is CollectionScreenEvent.SortByTitles -> sortListByTitle()

            is CollectionScreenEvent.SortByLikes -> sortListByLikes()
            is CollectionScreenEvent.SortByUpdatedDate -> sortListByUpdatedDate()
        }
    }

    private fun getCollectionsList() {
        viewModelScope.launch {
            repository.getCollectionsList().cachedIn(viewModelScope)
                .collectLatest { value: PagingData<WallpaperCollections> ->
                    value.let { list ->
                        _collectionPhotosState.value = list
                    }
                }
        }
    }


    private fun sortListByTitle() {
        viewModelScope.launch {
            repository.getCollectionsListByTitleSort().cachedIn(viewModelScope).collectLatest { sortedPagingData ->
                sortedPagingData.let { list ->
                    _collectionPhotosState.value = list
                }
            }
        }
    }

    private fun sortListByUpdatedDate() {
        viewModelScope.launch {
            repository.getCollectionsListByUpdateDateSort().cachedIn(viewModelScope).collectLatest { sortedPagingData ->
                sortedPagingData.let { list ->
                    _collectionPhotosState.value = list
                }
            }
        }
    }

    private fun sortListByLikes() {
        viewModelScope.launch {
            repository.getCollectionsListByLikesSort().cachedIn(viewModelScope).collectLatest { sortedPagingData ->
                sortedPagingData.let { list ->
                    _collectionPhotosState.value = list
                }
            }
        }
    }
}