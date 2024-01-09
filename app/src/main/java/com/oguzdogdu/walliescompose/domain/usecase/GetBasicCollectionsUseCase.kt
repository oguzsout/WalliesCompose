package com.oguzdogdu.walliescompose.domain.usecase

import androidx.paging.PagingData
import com.oguzdogdu.walliescompose.domain.model.collections.WallpaperCollections
import com.oguzdogdu.walliescompose.domain.repository.WallpaperRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetBasicCollectionUseCase @Inject constructor(private val repository: WallpaperRepository) {
    suspend operator fun invoke(): Flow<PagingData<WallpaperCollections>> =
        repository.getCollectionsList().distinctUntilChanged()
}