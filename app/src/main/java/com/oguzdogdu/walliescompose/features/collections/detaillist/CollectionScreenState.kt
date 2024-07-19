package com.oguzdogdu.walliescompose.features.collections.detaillist

import androidx.compose.runtime.Immutable
import com.oguzdogdu.walliescompose.domain.model.collections.Collection
import com.oguzdogdu.walliescompose.domain.model.collections.CollectionList

@Immutable
data class CollectionListState(
    val loading: Boolean = false,
    val errorMessage: String? = null,
    val collectionsLists: List<CollectionList>? = null
)

@Immutable
data class CollectionConstantInfoState(
    val loading: Boolean = false,
    val errorMessage: String? = null,
    val collection: Collection? = null
)

