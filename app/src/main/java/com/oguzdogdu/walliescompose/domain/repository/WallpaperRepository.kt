package com.oguzdogdu.walliescompose.domain.repository

import androidx.paging.PagingData
import com.oguzdogdu.walliescompose.domain.model.collections.Collection
import com.oguzdogdu.walliescompose.domain.model.collections.CollectionList
import com.oguzdogdu.walliescompose.domain.model.collections.WallpaperCollections
import com.oguzdogdu.walliescompose.domain.model.detail.Photo
import com.oguzdogdu.walliescompose.domain.model.favorites.FavoriteImages
import com.oguzdogdu.walliescompose.domain.model.latest.LatestImage
import com.oguzdogdu.walliescompose.domain.model.popular.PopularImage
import com.oguzdogdu.walliescompose.domain.model.random.RandomImage
import com.oguzdogdu.walliescompose.domain.model.search.SearchPhoto
import com.oguzdogdu.walliescompose.domain.model.topics.TopicDetail
import com.oguzdogdu.walliescompose.domain.model.topics.Topics
import com.oguzdogdu.walliescompose.domain.model.userpreferences.UserPreferences
import com.oguzdogdu.walliescompose.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface WallpaperRepository {
    suspend fun getHomeImagesByPopulars(): Flow<Resource<List<PopularImage>?>>
    suspend fun getHomeTopicsImages(): Flow<Resource<List<Topics>?>>
    suspend fun getCollectionsList(): Flow<PagingData<WallpaperCollections>>
    suspend fun getCollectionsListByTitleSort(): Flow<PagingData<WallpaperCollections>>
    suspend fun getCollectionsListByLikesSort(): Flow<PagingData<WallpaperCollections>>
    suspend fun getCollectionsListByUpdateDateSort(): Flow<PagingData<WallpaperCollections>>
    suspend fun insertImageToFavorites(favorite: FavoriteImages)
    suspend fun getFavorites(): Flow<List<FavoriteImages>?>
    suspend fun deleteFavorites(favorite: FavoriteImages)
    suspend fun deleteSpecificIdFavorite(favoriteId: String)
    suspend fun getPhoto(id: String?): Flow<Resource<Photo?>>
    suspend fun searchPhoto(query: String?,language:String?): Flow<PagingData<SearchPhoto>>
    suspend fun getTopicsTitleWithPaging(): Flow<PagingData<Topics>>
    suspend fun getTopicListWithPaging(idOrSlug:String?): Flow<PagingData<TopicDetail>>
    suspend fun getCollectionsListById(id: String?): Flow<Resource<List<CollectionList>?>>
    suspend fun getImagesByPopulars(): Flow<PagingData<PopularImage>>
    suspend fun getImagesByLatest(): Flow<PagingData<LatestImage>>
    suspend fun getACollection(id: String?) : Flow<Resource<Collection>>
    suspend fun getRandomImages(count:Int?) : Flow<Resource<List<RandomImage>?>>
    suspend fun insertRecentSearchKeysToDB(userPreferences: UserPreferences)
    suspend fun getRecentSearchKeysFromDB(): Flow<List<UserPreferences>?>
    suspend fun deleteRecentSearchKeysFromDB(keyword:String?)

}