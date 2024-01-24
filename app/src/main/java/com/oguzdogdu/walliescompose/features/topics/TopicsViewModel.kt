package com.oguzdogdu.walliescompose.features.topics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.oguzdogdu.walliescompose.domain.model.collections.WallpaperCollections
import com.oguzdogdu.walliescompose.domain.model.topics.Topics
import com.oguzdogdu.walliescompose.domain.repository.WallpaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopicsViewModel @Inject constructor(
    private val repository: WallpaperRepository
) : ViewModel() {
    private val _getTopics = MutableStateFlow(TopicsScreenState())
    val getTopics = _getTopics.asStateFlow()

    private val _topicsState: MutableStateFlow<PagingData<Topics>> = MutableStateFlow(value = PagingData.empty())
    val topicsState: MutableStateFlow<PagingData<Topics>> get() = _topicsState

    fun handleUIEvent(event: TopicsScreenEvent) {
        when (event) {
            is TopicsScreenEvent.FetchTopicsData -> {
                getTopicsList()
            }
        }
    }

    private fun getTopicsList() {
        viewModelScope.launch {
            repository.getTopicsTitleWithPaging().cachedIn(viewModelScope).collect { topics ->
                _getTopics.update { it.copy(topics = topics) }
                _topicsState.value = topics
            }
        }
    }
}
