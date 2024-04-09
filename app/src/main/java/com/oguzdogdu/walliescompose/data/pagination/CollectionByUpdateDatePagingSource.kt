package com.oguzdogdu.walliescompose.data.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.oguzdogdu.walliescompose.data.common.Constants
import com.oguzdogdu.walliescompose.data.model.collection.CollectionResponse
import com.oguzdogdu.walliescompose.data.service.WallpaperService
import com.oguzdogdu.walliescompose.util.formatDate

class CollectionByUpdateDatePagingSource(private val service: WallpaperService) :
PagingSource<Int, CollectionResponse>() {
    override fun getRefreshKey(state: PagingState<Int, CollectionResponse>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CollectionResponse> {
        return try {
            val page = params.key ?: 1
            val response =
                service.getCollections(page = page, perPage = Constants.PAGE_ITEM_LIMIT).body()
                    .orEmpty()
            val filteredAndSortedResponse = response.sortedByDescending { it.updated_at?.formatDate() }
            LoadResult.Page(
                data = filteredAndSortedResponse,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            PagingSource.LoadResult.Error(e)
        }
    }
}