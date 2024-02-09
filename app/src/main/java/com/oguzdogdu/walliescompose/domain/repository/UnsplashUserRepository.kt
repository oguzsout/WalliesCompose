package com.oguzdogdu.walliescompose.domain.repository

import com.oguzdogdu.walliescompose.domain.model.userdetail.UserCollections
import com.oguzdogdu.walliescompose.domain.model.userdetail.UserDetails
import com.oguzdogdu.walliescompose.domain.model.userdetail.UsersPhotos
import com.oguzdogdu.walliescompose.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface UnsplashUserRepository {
    suspend fun getUserDetails(username: String?): Flow<Resource<UserDetails?>>
    suspend fun getUsersPhotos(username: String?): Flow<Resource<List<UsersPhotos>?>>
    suspend fun getUsersCollections(username: String?): Flow<Resource<List<UserCollections>?>>
}