package com.oguzdogdu.walliescompose.features.collections.detaillist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.walliescompose.domain.repository.WallpaperRepository
import com.oguzdogdu.walliescompose.domain.wrapper.onFailure
import com.oguzdogdu.walliescompose.domain.wrapper.onLoading
import com.oguzdogdu.walliescompose.domain.wrapper.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionDetailListViewModel  @Inject constructor(
    private val repository: WallpaperRepository
) :
    ViewModel() {

    private val _getCollectionDetail = MutableStateFlow(CollectionsListsState())
    val getCollectionDetail = _getCollectionDetail.asStateFlow()

    fun handleUiEvent(event: CollectionListEvent) {
        when (event) {
            is CollectionListEvent.FetchCollectionList -> {
                getCollectionsLists(id = event.id)
            }
        }
    }

    private fun getCollectionsLists(id: String?) {
        viewModelScope.launch {
            repository.getCollectionsListById(id).collect { result ->
                result.onLoading {
                    _getCollectionDetail.update { it.copy(loading = true) }
                }

                result.onSuccess { list ->
                    list?.let {
                        _getCollectionDetail.update {
                            it.copy(loading = false, collectionsLists = list)
                        }
                    }
                }

                result.onFailure { error ->
                    _getCollectionDetail.update {
                       it.copy(errorMessage = error)
                    }
                }
            }
        }
    }
}