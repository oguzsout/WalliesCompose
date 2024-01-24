package com.oguzdogdu.walliescompose.domain.repository

import androidx.paging.PagingData
import com.oguzdogdu.walliescompose.domain.model.collections.WallpaperCollections
import com.oguzdogdu.walliescompose.domain.model.detail.Photo
import com.oguzdogdu.walliescompose.domain.model.favorites.FavoriteImages
import com.oguzdogdu.walliescompose.domain.model.latest.LatestImage
import com.oguzdogdu.walliescompose.domain.model.popular.PopularImage
import com.oguzdogdu.walliescompose.domain.model.search.SearchPhoto
import com.oguzdogdu.walliescompose.domain.model.topics.TopicDetail
import com.oguzdogdu.walliescompose.domain.model.topics.Topics
import com.oguzdogdu.walliescompose.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface WallpaperRepository {
    suspend fun getHomeImagesByPopulars(): Flow<Resource<List<PopularImage>?>>
    suspend fun getHomeImagesByLatest(): Flow<Resource<List<LatestImage>?>>
    suspend fun getHomeTopicsImages(): Flow<Resource<List<Topics>?>>
    suspend fun getCollectionsList(): Flow<PagingData<WallpaperCollections>>
    suspend fun getCollectionsListByTitleSort(): Flow<PagingData<WallpaperCollections>>
    suspend fun getCollectionsListByLikesSort(): Flow<PagingData<WallpaperCollections>>
    suspend fun insertImageToFavorites(favorite: FavoriteImages)
    suspend fun getFavorites(): Flow<Resource<List<FavoriteImages>?>>
    suspend fun deleteFavorites(favorite: FavoriteImages)
    suspend fun getPhoto(id: String?): Flow<Resource<Photo?>>
    suspend fun searchPhoto(query: String?,language:String?): Flow<PagingData<SearchPhoto>>
    suspend fun getTopicsTitleWithPaging(): Flow<PagingData<Topics>>
    suspend fun getTopicListWithPaging(idOrSlug:String?): Flow<PagingData<TopicDetail>>
}