package com.oguzdogdu.walliescompose.data.service

import com.oguzdogdu.walliescompose.data.model.topics.TopicsResponseItem
import com.oguzdogdu.walliescompose.data.model.maindto.UnsplashResponseItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WallpaperService {
    @GET("photos")
    suspend fun getImagesByOrders(
        @Query("per_page") perPage: Int?,
        @Query("page") page: Int?,
        @Query("order_by") order: String?,
    ): Response<List<UnsplashResponseItem>>

    @GET("topics")
    suspend fun getTopics(
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
    ): Response<List<TopicsResponseItem>>
}