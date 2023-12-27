package com.oguzdogdu.walliescompose.data

import com.oguzdogdu.walliescompose.data.common.safeApiCall
import com.oguzdogdu.walliescompose.data.di.Dispatcher
import com.oguzdogdu.walliescompose.data.di.WalliesDispatchers
import com.oguzdogdu.walliescompose.data.model.toDomainTopics
import com.oguzdogdu.walliescompose.data.service.WallpaperService
import com.oguzdogdu.walliescompose.domain.model.Topics
import com.oguzdogdu.walliescompose.domain.repository.WallpaperRepository
import com.oguzdogdu.walliescompose.domain.wrapper.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WallpaperRepositoryImpl @Inject constructor(
    private val service: WallpaperService,
    @Dispatcher(WalliesDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) :
    WallpaperRepository {

        override suspend fun getTopicsTitle(): Flow<Resource<List<Topics>?>> {
        return safeApiCall(ioDispatcher) {
            service.getTopics(perPage = 6, page = 1).body()?.map {
                it.toDomainTopics()
            }
        }
    }
}