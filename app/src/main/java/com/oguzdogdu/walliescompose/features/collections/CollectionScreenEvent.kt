package com.oguzdogdu.walliescompose.features.collections

import com.oguzdogdu.walliescompose.features.detail.DetailScreenEvent

sealed class CollectionScreenEvent {
    data object FetchLatestData : CollectionScreenEvent()
    data object SortByTitles : CollectionScreenEvent()
    data object SortByLikes : CollectionScreenEvent()
    data object SortByUpdatedDate: CollectionScreenEvent()
    data class OpenFilterBottomSheet(val isOpen: Boolean) : CollectionScreenEvent()
    data class ChoisedFilterOption(val choised: Int) : CollectionScreenEvent()
}
