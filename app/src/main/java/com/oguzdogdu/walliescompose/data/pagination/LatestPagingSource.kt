package com.oguzdogdu.walliescompose.data.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.oguzdogdu.walliescompose.data.common.Constants
import com.oguzdogdu.walliescompose.data.common.Constants.PAGE_ITEM_LIMIT
import com.oguzdogdu.walliescompose.data.model.maindto.UnsplashResponseItem
import com.oguzdogdu.walliescompose.data.service.WallpaperService

class LatestPagingSource(private val service: WallpaperService) :
    PagingSource<Int, UnsplashResponseItem>() {

    override fun getRefreshKey(state: PagingState<Int, UnsplashResponseItem>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashResponseItem> {
        return try {
            val page = params.key ?: 1
            val response = service.getImagesByOrders(
                perPage = PAGE_ITEM_LIMIT,
                page = page,
                order = Constants.LATEST
            ).body().orEmpty()
            LoadResult.Page(
                data = response,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}