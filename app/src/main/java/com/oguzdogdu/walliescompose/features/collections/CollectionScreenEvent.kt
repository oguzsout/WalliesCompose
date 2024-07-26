package com.oguzdogdu.walliescompose.features.collections

sealed class CollectionScreenEvent {
    data object FetchLatestData : CollectionScreenEvent()
    data object SortByTitles : CollectionScreenEvent()
    data object SortByLikes : CollectionScreenEvent()
    data object SortByUpdatedDate: CollectionScreenEvent()
    data object CheckListType: CollectionScreenEvent()
    data class OpenFilterBottomSheet(val isOpen: Boolean) : CollectionScreenEvent()
    data class ChoisedFilterOption(val choised: Int) : CollectionScreenEvent()
    data class ChangeListType(val listType: ListType) : CollectionScreenEvent()
}
