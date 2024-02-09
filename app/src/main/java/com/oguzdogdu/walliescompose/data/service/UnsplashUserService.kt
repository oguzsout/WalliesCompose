package com.oguzdogdu.walliescompose.data.service

import com.oguzdogdu.walliescompose.data.model.userdetail.UserDetailResponse
import com.oguzdogdu.walliescompose.data.model.collection.CollectionResponse
import com.oguzdogdu.walliescompose.data.model.maindto.Photo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UnsplashUserService {
    @GET("users/{username}")
    suspend fun getUserDetailInfos(
        @Path("username") username: String?,
    ): Response<UserDetailResponse>

    @GET("users/{username}/photos")
    suspend fun getUserPhotos(
        @Path("username") username: String?,
    ): Response<List<Photo>>

    @GET("users/{username}/collections")
    suspend fun getUserCollections(
        @Path("username") username: String?,
    ): Response<List<CollectionResponse>>
}