package com.oguzdogdu.walliescompose.features.topics.topicdetaillist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.oguzdogdu.walliescompose.domain.model.topics.TopicDetail
import com.oguzdogdu.walliescompose.domain.model.topics.Topics
import com.oguzdogdu.walliescompose.domain.repository.WallpaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopicDetailListViewModel @Inject constructor(
    private val repository: WallpaperRepository
) : ViewModel() {

    private val _getTopicList = MutableStateFlow(TopicListState())
    val getTopicList = _getTopicList.asStateFlow()

    private val _topicListState: MutableStateFlow<PagingData<TopicDetail>> = MutableStateFlow(value = PagingData.empty())
    val topicListState: MutableStateFlow<PagingData<TopicDetail>> get() = _topicListState

    fun handleUIEvent(event: TopicDetailListEvent) {
        when (event) {
            is TopicDetailListEvent.FetchTopicListData -> {
                getTopicDetailList(idOrSlug = event.idOrSlug)
            }
        }
    }

    private fun getTopicDetailList(idOrSlug: String?) {
        viewModelScope.launch {
            repository.getTopicListWithPaging(idOrSlug = idOrSlug).cachedIn(viewModelScope).collect { topicList ->
                topicList.let { list ->
                    _getTopicList.update { it.copy(topicList = topicList) }
                    _topicListState.value = topicList
                }
            }
        }
    }
}