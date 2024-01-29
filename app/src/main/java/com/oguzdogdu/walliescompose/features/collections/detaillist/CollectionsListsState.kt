package com.oguzdogdu.walliescompose.features.collections.detaillist

import com.oguzdogdu.walliescompose.domain.model.collections.CollectionList

data class CollectionsListsState(
    val loading: Boolean = false,
    val errorMessage: String? = null,
    val collectionsLists: List<CollectionList>? = null
)

