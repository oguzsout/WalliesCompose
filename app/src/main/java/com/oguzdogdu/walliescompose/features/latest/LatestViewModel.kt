package com.oguzdogdu.walliescompose.features.latest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.oguzdogdu.walliescompose.domain.model.latest.LatestImage
import com.oguzdogdu.walliescompose.domain.repository.WallpaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LatestViewModel @Inject constructor(private val repository: WallpaperRepository) : ViewModel() {

    private val _getLatest: MutableStateFlow<PagingData<LatestImage>> = MutableStateFlow(
        PagingData.empty()
    )
    val getLatest = _getLatest.asStateFlow()

    fun handleUIEvent(event: LatestScreenEvent) {
        when (event) {
            is LatestScreenEvent.FetchLatestData -> {
                getLatestImages()
            }
        }
    }

    private fun getLatestImages() {
        viewModelScope.launch {
            repository.getImagesByLatest().cachedIn(viewModelScope).collect { latest ->
               _getLatest.value = latest
            }
        }
    }
}
