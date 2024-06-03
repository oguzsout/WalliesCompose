package com.oguzdogdu.walliescompose.data.service

import com.oguzdogdu.walliescompose.data.model.collection.CollectionResponse
import com.oguzdogdu.walliescompose.data.model.maindto.Photo
import com.oguzdogdu.walliescompose.data.model.searchdto.searchuser.SearchUsersResponse
import com.oguzdogdu.walliescompose.data.model.userdetail.UserDetailResponse

interface UnsplashUserService {
    suspend fun getUserDetailInfos(
        username: String?,
    ): UserDetailResponse

    suspend fun getUserPhotos(
        username: String?,
    ): List<Photo>

    suspend fun getUserCollections(
        username: String?,
    ): List<CollectionResponse>

    suspend fun getSearchUserData(
        page: Int?,
        query: String?,
    ): SearchUsersResponse
}