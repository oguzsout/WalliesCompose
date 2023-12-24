package com.oguzdogdu.walliescompose.domain.repository

import com.oguzdogdu.walliescompose.domain.common.Resource
import com.oguzdogdu.walliescompose.domain.model.Topics
import kotlinx.coroutines.flow.Flow

interface WallpaperRepository {
    suspend fun getTopicsTitle(): Flow<Resource<List<Topics>?>>
}