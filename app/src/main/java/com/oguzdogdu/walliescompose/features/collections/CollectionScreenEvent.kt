package com.oguzdogdu.walliescompose.features.collections

sealed class CollectionScreenEvent {
    data object FetchLatestData : CollectionScreenEvent()
    data object SortByTitles : CollectionScreenEvent()
    data object SortByLikes : CollectionScreenEvent()
}
