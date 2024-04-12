package com.oguzdogdu.walliescompose.features.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.oguzdogdu.walliescompose.domain.model.collections.WallpaperCollections
import com.oguzdogdu.walliescompose.domain.repository.WallpaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionsViewModel @Inject constructor(private val repository: WallpaperRepository) :
    ViewModel() {

    private val _collectionPhotosState: MutableStateFlow<PagingData<WallpaperCollections>> = MutableStateFlow(value = PagingData.empty())
    val collectionPhotosState = _collectionPhotosState.asStateFlow()

    private val _filterBottomSheetOpenStat = MutableStateFlow(false)
    val filterBottomSheetOpenStat = _filterBottomSheetOpenStat.asStateFlow()

    private val _choisedFilter = MutableStateFlow(0)
    val choisedFilter = _choisedFilter.asStateFlow()

    fun handleUIEvent(event: CollectionScreenEvent) {
        when (event) {
            is CollectionScreenEvent.FetchLatestData -> getCollectionsList()
            is CollectionScreenEvent.SortByTitles -> sortListByTitle()
            is CollectionScreenEvent.SortByLikes -> sortListByLikes()
            is CollectionScreenEvent.SortByUpdatedDate -> sortListByUpdatedDate()
            is CollectionScreenEvent.OpenFilterBottomSheet -> _filterBottomSheetOpenStat.value =
                event.isOpen
            is CollectionScreenEvent.ChoisedFilterOption -> _choisedFilter.value = event.choised
        }
    }

    private fun getCollectionsList() {
        viewModelScope.launch {
            async {
                repository.getCollectionsList().cachedIn(viewModelScope)
                    .collectLatest { value: PagingData<WallpaperCollections> ->
                        value.let { list ->
                            _collectionPhotosState.value = list
                        }
                    }
            }.await()
        }
    }

    private fun sortListByTitle() {
        viewModelScope.launch {
            async { _collectionPhotosState.value = PagingData.empty() }.await()
            async {
                repository.getCollectionsListByTitleSort().cachedIn(viewModelScope)
                    .collectLatest { sortedPagingData ->
                        sortedPagingData.let { list ->
                            _collectionPhotosState.value = list
                        }
                    }
            }.await()
        }
    }

    private fun sortListByUpdatedDate() {
        viewModelScope.launch {
            async { _collectionPhotosState.value = PagingData.empty() }.await()
            async {
                repository.getCollectionsListByUpdateDateSort().cachedIn(viewModelScope)
                    .collectLatest { sortedPagingData ->
                        sortedPagingData.let { list ->
                            _collectionPhotosState.value = list
                        }
                    }
            }.await()
        }
    }

    private fun sortListByLikes() {
        viewModelScope.launch {
            async { _collectionPhotosState.value = PagingData.empty() }.await()
            async {
                repository.getCollectionsListByLikesSort().cachedIn(viewModelScope)
                    .collectLatest { sortedPagingData ->
                        sortedPagingData.let { list ->
                            _collectionPhotosState.value = list
                        }
                    }
            }.await()
        }
    }
}