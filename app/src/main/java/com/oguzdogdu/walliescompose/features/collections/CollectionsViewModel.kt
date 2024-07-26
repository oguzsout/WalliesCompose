package com.oguzdogdu.walliescompose.features.collections

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.oguzdogdu.walliescompose.domain.model.collections.WallpaperCollections
import com.oguzdogdu.walliescompose.domain.repository.WallpaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: WallpaperRepository
) : ViewModel() {

    private val listType = savedStateHandle.getStateFlow("ListType", "")

    private val _collectionPhotosState: MutableStateFlow<PagingData<WallpaperCollections>> =
        MutableStateFlow(value = PagingData.empty())
    val collectionPhotosState = _collectionPhotosState.asStateFlow()

    private val _collectionScreenState: MutableStateFlow<CollectionState> = MutableStateFlow(
        CollectionState()
    )
    val collectionScreenState = _collectionScreenState.asStateFlow()

    fun handleUIEvent(event: CollectionScreenEvent) {
        when (event) {
            is CollectionScreenEvent.FetchLatestData -> getCollectionsList()
            is CollectionScreenEvent.SortByTitles -> sortListByTitle()
            is CollectionScreenEvent.SortByLikes -> sortListByLikes()
            is CollectionScreenEvent.SortByUpdatedDate -> sortListByUpdatedDate()
            is CollectionScreenEvent.OpenFilterBottomSheet -> setFilterBottomSheetState(event.isOpen)
            is CollectionScreenEvent.ChoisedFilterOption -> setChoisedFilter(event.choised)
            is CollectionScreenEvent.ChangeListType -> changeListType(event.listType.toString())

            is CollectionScreenEvent.CheckListType -> changeListType(listType.value)
        }
    }

    fun onListTypeChanged(newType: String) {
        savedStateHandle["ListType"] = newType
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

    private fun setFilterBottomSheetState(isOpen: Boolean) {
        viewModelScope.launch {
            _collectionScreenState.update {
                it.copy(sheetState = isOpen)
            }
        }
    }

    private fun setChoisedFilter(choised: Int) {
        viewModelScope.launch {
            _collectionScreenState.update {
                it.copy(choisedFilter = choised)
            }
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

    private fun changeListType(listType: String) {
        viewModelScope.launch {
            _collectionScreenState.update {
                when(listType) {
                    ListType.VERTICAL.name -> {
                        it.copy(collectionsListType = ListType.VERTICAL)
                    }
                    ListType.GRID.name -> {
                        it.copy(collectionsListType = ListType.GRID)
                    }
                    else -> it.copy()
                }
            }
        }
    }
}