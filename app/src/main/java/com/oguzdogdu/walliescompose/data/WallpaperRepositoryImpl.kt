package com.oguzdogdu.walliescompose.data

import com.oguzdogdu.walliescompose.data.common.Constants.LATEST
import com.oguzdogdu.walliescompose.data.common.Constants.POPULAR
import com.oguzdogdu.walliescompose.data.common.safeApiCall
import com.oguzdogdu.walliescompose.data.di.Dispatcher
import com.oguzdogdu.walliescompose.data.di.WalliesDispatchers
import com.oguzdogdu.walliescompose.data.model.maindto.toDomainModelLatest
import com.oguzdogdu.walliescompose.data.model.maindto.toDomainModelPopular
import com.oguzdogdu.walliescompose.data.model.topics.toDomainTopics
import com.oguzdogdu.walliescompose.data.service.WallpaperService
import com.oguzdogdu.walliescompose.domain.model.latest.LatestImage
import com.oguzdogdu.walliescompose.domain.model.popular.PopularImage
import com.oguzdogdu.walliescompose.domain.model.topics.Topics
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
    override suspend fun getHomeImagesByPopulars(): Flow<Resource<List<PopularImage>?>> {
        return safeApiCall(ioDispatcher) {
            service.getImagesByOrders(perPage = 10, page = 1, order = POPULAR).body()?.map {
                it.toDomainModelPopular()
            }
        }
    }

    override suspend fun getHomeImagesByLatest(): Flow<Resource<List<LatestImage>?>> {
        return safeApiCall(ioDispatcher) {
            service.getImagesByOrders(perPage = 10, page = 1, order = LATEST).body()?.map {
                it.toDomainModelLatest()
            }
        }
    }

    override suspend fun getHomeTopicsImages(): Flow<Resource<List<Topics>?>> {
        return safeApiCall(ioDispatcher) {
            service.getTopics(perPage = 6, page = 1).body()?.map {
                it.toDomainTopics()
            }
        }
    }
}