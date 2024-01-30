package com.oguzdogdu.walliescompose.features.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.oguzdogdu.walliescompose.domain.model.popular.PopularImage
import com.oguzdogdu.walliescompose.domain.model.topics.TopicDetail
import com.oguzdogdu.walliescompose.domain.repository.WallpaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularViewModel @Inject constructor(
    private val repository: WallpaperRepository
) : ViewModel() {

    private val _getPopular : MutableStateFlow<PagingData<PopularImage>> = MutableStateFlow(PagingData.empty())
    val getPopular = _getPopular.asStateFlow()

    fun handleUIEvent(event: PopularScreenEvent) {
        when (event) {
            PopularScreenEvent.FetchPopularData -> {
                getPopularImages()
            }
        }
    }

    private fun getPopularImages() {
        viewModelScope.launch {
            repository.getImagesByPopulars().cachedIn(viewModelScope).collect { popular ->
                _getPopular.value = popular
            }
        }
    }
}
