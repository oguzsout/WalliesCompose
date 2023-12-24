package com.oguzdogdu.walliescompose.data.service

import com.oguzdogdu.walliescompose.data.model.topics.TopicsResponseItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WallpaperService {
    @GET("topics")
    suspend fun getTopics(
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
    ): Response<List<TopicsResponseItem>>
}