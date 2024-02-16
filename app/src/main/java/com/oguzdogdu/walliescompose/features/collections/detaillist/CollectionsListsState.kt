package com.oguzdogdu.walliescompose.features.collections.detaillist

import androidx.compose.runtime.Immutable
import com.oguzdogdu.walliescompose.domain.model.collections.CollectionList

@Immutable
data class CollectionsListsState(
    val loading: Boolean = false,
    val errorMessage: String? = null,
    val collectionsLists: List<CollectionList>? = null
)

