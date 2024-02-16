package com.oguzdogdu.walliescompose.features.search

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import com.oguzdogdu.walliescompose.domain.model.search.SearchPhoto
@Immutable
data class SearchState(val searchPhoto: PagingData<SearchPhoto> = PagingData.empty())
