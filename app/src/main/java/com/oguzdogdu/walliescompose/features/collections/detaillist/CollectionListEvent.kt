package com.oguzdogdu.walliescompose.features.collections.detaillist

sealed class CollectionListEvent {
    data class FetchCollectionList(val id: String?) : CollectionListEvent()
}
