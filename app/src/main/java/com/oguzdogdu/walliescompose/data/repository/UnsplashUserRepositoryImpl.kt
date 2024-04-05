package com.oguzdogdu.walliescompose.data.repository

import com.oguzdogdu.walliescompose.data.common.Constants.PAGE_ITEM_LIMIT
import com.oguzdogdu.walliescompose.data.common.safeApiCall
import com.oguzdogdu.walliescompose.data.di.Dispatcher
import com.oguzdogdu.walliescompose.data.di.WalliesDispatchers
import com.oguzdogdu.walliescompose.data.model.collection.toUserCollection
import com.oguzdogdu.walliescompose.data.model.maindto.toDomainUsersPhotos
import com.oguzdogdu.walliescompose.data.model.userdetail.toDomain
import com.oguzdogdu.walliescompose.data.service.UnsplashUserService
import com.oguzdogdu.walliescompose.domain.model.userdetail.UserCollections
import com.oguzdogdu.walliescompose.domain.model.userdetail.UserDetails
import com.oguzdogdu.walliescompose.domain.model.userdetail.UsersPhotos
import com.oguzdogdu.walliescompose.domain.repository.UnsplashUserRepository
import com.oguzdogdu.walliescompose.domain.wrapper.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UnsplashUserRepositoryImpl @Inject constructor(private val service: UnsplashUserService, @Dispatcher(
    WalliesDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) :
    UnsplashUserRepository {
    override suspend fun getUserDetails(username: String?): Flow<Resource<UserDetails?>> {
        return safeApiCall(ioDispatcher) {
            service.getUserDetailInfos(username = username).body()?.toDomain()
        }
    }

    override suspend fun getUsersPhotos(username: String?): Flow<Resource<List<UsersPhotos>?>>{
        return safeApiCall(ioDispatcher) {
            service.getUserPhotos(username = username, perPage = PAGE_ITEM_LIMIT).body().orEmpty().map {
                it.toDomainUsersPhotos()
            }
        }
    }

    override suspend fun getUsersCollections(username: String?): Flow<Resource<List<UserCollections>?>> {
        return safeApiCall(dispatcher = ioDispatcher) {
            service.getUserCollections(username = username).body().orEmpty().map {
                it.toUserCollection()
            }
        }
    }
}