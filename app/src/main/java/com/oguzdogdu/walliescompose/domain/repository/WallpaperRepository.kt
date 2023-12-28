package com.oguzdogdu.walliescompose.domain.repository

import com.oguzdogdu.walliescompose.domain.model.latest.LatestImage
import com.oguzdogdu.walliescompose.domain.model.popular.PopularImage
import com.oguzdogdu.walliescompose.domain.model.topics.Topics
import com.oguzdogdu.walliescompose.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface WallpaperRepository {
    suspend fun getHomeImagesByPopulars(): Flow<Resource<List<PopularImage>?>>
    suspend fun getHomeImagesByLatest(): Flow<Resource<List<LatestImage>?>>
    suspend fun getHomeTopicsImages(): Flow<Resource<List<Topics>?>>
}