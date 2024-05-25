package com.oguzdogdu.walliescompose.data.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.oguzdogdu.walliescompose.data.model.searchdto.searchuser.Result
import com.oguzdogdu.walliescompose.data.service.UnsplashUserService

class SearchUsersPagingSource(
    private val service: UnsplashUserService,
    private val query: String,
) : PagingSource<Int, Result>() {

    override fun getRefreshKey(
        state: PagingState<Int, Result>,
    ): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>)
            : LoadResult<Int, Result> {
        return try {
            val page = params.key ?: 1
            val response = service.getSearchUserData(
                page = page,
                query = query
            ).body()?.results.orEmpty()
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