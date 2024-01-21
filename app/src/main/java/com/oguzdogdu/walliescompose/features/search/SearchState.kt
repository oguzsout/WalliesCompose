package com.oguzdogdu.walliescompose.features.search

import androidx.paging.PagingData
import com.oguzdogdu.walliescompose.domain.model.search.SearchPhoto

data class SearchState(val searchPhoto: PagingData<SearchPhoto> = PagingData.empty())
