package com.oguzdogdu.walliescompose.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.oguzdogdu.walliescompose.data.model.searchdto.toDomainSearch
import com.oguzdogdu.walliescompose.cache.dao.FavoriteDao
import com.oguzdogdu.walliescompose.cache.entity.FavoriteImage
import com.oguzdogdu.walliescompose.cache.entity.toDomain
import com.oguzdogdu.walliescompose.data.common.Constants
import com.oguzdogdu.walliescompose.data.common.Constants.PAGE_ITEM_LIMIT
import com.oguzdogdu.walliescompose.data.common.safeApiCall
import com.oguzdogdu.walliescompose.data.di.Dispatcher
import com.oguzdogdu.walliescompose.data.di.WalliesDispatchers
import com.oguzdogdu.walliescompose.data.model.collection.toCollectionDomain
import com.oguzdogdu.walliescompose.data.model.maindto.toDomainCollectionDetailList
import com.oguzdogdu.walliescompose.data.model.maindto.toDomainModelLatest
import com.oguzdogdu.walliescompose.data.model.maindto.toDomainModelPhoto
import com.oguzdogdu.walliescompose.data.model.maindto.toDomainModelPopular
import com.oguzdogdu.walliescompose.data.model.topics.toDomainTopicList
import com.oguzdogdu.walliescompose.data.model.topics.toDomainTopics
import com.oguzdogdu.walliescompose.data.pagination.CollectionByLikesPagingSource
import com.oguzdogdu.walliescompose.data.pagination.CollectionsByTitlePagingSource
import com.oguzdogdu.walliescompose.data.pagination.CollectionsPagingSource
import com.oguzdogdu.walliescompose.data.pagination.SearchPagingSource
import com.oguzdogdu.walliescompose.data.pagination.TopicListSource
import com.oguzdogdu.walliescompose.data.pagination.TopicsPagingSource
import com.oguzdogdu.walliescompose.data.service.WallpaperService
import com.oguzdogdu.walliescompose.domain.model.collections.CollectionList
import com.oguzdogdu.walliescompose.domain.model.collections.WallpaperCollections
import com.oguzdogdu.walliescompose.domain.model.detail.Photo
import com.oguzdogdu.walliescompose.domain.model.favorites.FavoriteImages
import com.oguzdogdu.walliescompose.domain.model.latest.LatestImage
import com.oguzdogdu.walliescompose.domain.model.popular.PopularImage
import com.oguzdogdu.walliescompose.domain.model.search.SearchPhoto
import com.oguzdogdu.walliescompose.domain.model.topics.TopicDetail
import com.oguzdogdu.walliescompose.domain.model.topics.Topics
import com.oguzdogdu.walliescompose.domain.repository.WallpaperRepository
import com.oguzdogdu.walliescompose.domain.wrapper.Resource
import com.oguzdogdu.walliescompose.domain.wrapper.toResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class WallpaperRepositoryImpl @Inject constructor(
    private val service: WallpaperService,
    private val favoriteDao: FavoriteDao,
    @Dispatcher(WalliesDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) :
    WallpaperRepository {
    override suspend fun getHomeImagesByPopulars(): Flow<Resource<List<PopularImage>?>> {
        return safeApiCall(ioDispatcher) {
            service.getImagesByOrders(perPage = 10, page = 1, order = Constants.POPULAR).body()
                ?.map {
                    it.toDomainModelPopular()
                }
        }
    }

    override suspend fun getHomeImagesByLatest(): Flow<Resource<List<LatestImage>?>> {
        return safeApiCall(ioDispatcher) {
            service.getImagesByOrders(perPage = 10, page = 1, order = Constants.LATEST).body()
                ?.map {
                    it.toDomainModelLatest()
                }
        }
    }

    override suspend fun getHomeTopicsImages(): Flow<Resource<List<Topics>?>> {
        return safeApiCall(ioDispatcher) {
            service.getTopics(perPage = 6, page = 1).body()?.map {
                it.toDomainTopics()
            }
        }
    }

    override suspend fun getCollectionsList(): Flow<PagingData<WallpaperCollections>> {
        val pagingConfig = PagingConfig(pageSize = PAGE_ITEM_LIMIT)
        return Pager(
            config = pagingConfig,
            initialKey = 1,
            pagingSourceFactory = { CollectionsPagingSource(service = service) }
        ).flow.mapNotNull {
            it.map { collection ->
                collection.toCollectionDomain()
            }
        }
    }

    override suspend fun getCollectionsListByTitleSort(): Flow<PagingData<WallpaperCollections>> {
        val pagingConfig = PagingConfig(pageSize = PAGE_ITEM_LIMIT)
        return Pager(
            config = pagingConfig,
            initialKey = 1,
            pagingSourceFactory = { CollectionsByTitlePagingSource(service = service) }
        ).flow.mapNotNull {
            it.map { collection ->
                collection.toCollectionDomain()
            }
        }
    }

    override suspend fun getCollectionsListByLikesSort(): Flow<PagingData<WallpaperCollections>> {
        val pagingConfig = PagingConfig(pageSize = PAGE_ITEM_LIMIT)
        return Pager(
            config = pagingConfig,
            initialKey = 1,
            pagingSourceFactory = { CollectionByLikesPagingSource(service = service) }
        ).flow.mapNotNull {
            it.map { collection ->
                collection.toCollectionDomain()
            }
        }
    }

    override suspend fun getFavorites():  Flow<Resource<List<FavoriteImages>?>> {
        return favoriteDao.getFavorites().map { list ->
            list.map {
                it.toDomain()
            }
        }.toResource()
    }
    override suspend fun insertImageToFavorites(favorite: FavoriteImages) {
        return favoriteDao.addFavorites(
            FavoriteImage(
                id = favorite.id.orEmpty(),
                url = favorite.url,
                profileImage = favorite.profileImage,
                name = favorite.name,
                portfolioUrl = favorite.portfolioUrl,
                isChecked = favorite.isChecked
            )
        )
    }

    override suspend fun deleteFavorites(favorite: FavoriteImages) {
        return favoriteDao.deleteFavorite(
            FavoriteImage(
                id = favorite.id.orEmpty(),
                url = favorite.url,
                profileImage = favorite.profileImage,
                name = favorite.name,
                portfolioUrl = favorite.portfolioUrl,
                isChecked = favorite.isChecked
            )
        )
    }
    override suspend fun getPhoto(id: String?): Flow<Resource<Photo?>> {
        return safeApiCall(ioDispatcher) {
            service.getPhoto(id = id).body()?.toDomainModelPhoto()
        }
    }

    override suspend fun searchPhoto(
        query: String?,
        language: String?
    ): Flow<PagingData<SearchPhoto>> {
        val pagingConfig = PagingConfig(pageSize = PAGE_ITEM_LIMIT)
        return Pager(
            config = pagingConfig,
            initialKey = 1,
            pagingSourceFactory = { SearchPagingSource(service = service, query = query ?: "",lang = language) }
        ).flow.mapNotNull {
            it.map { search ->
                search.toDomainSearch()
            }
        }
    }

    override suspend fun getTopicsTitleWithPaging(): Flow<PagingData<Topics>> {
        val pagingConfig = PagingConfig(pageSize = PAGE_ITEM_LIMIT)
        return Pager(
            config = pagingConfig,
            initialKey = 1,
            pagingSourceFactory = { TopicsPagingSource(service = service) }
        ).flow.mapNotNull {
            it.map { topics ->
                topics.toDomainTopics()
            }
        }
    }

    override suspend fun getTopicListWithPaging(idOrSlug:String?): Flow<PagingData<TopicDetail>> {
        val pagingConfig = PagingConfig(pageSize = PAGE_ITEM_LIMIT)
        return Pager(
            config = pagingConfig,
            initialKey = 1,
            pagingSourceFactory = { TopicListSource(service = service, idOrSlug = idOrSlug) }
        ).flow.mapNotNull {
            it.map { topicList ->
                topicList.toDomainTopicList()
            }
        }
    }
    override suspend fun getCollectionsListById(id: String?): Flow<Resource<List<CollectionList>?>> {
        return safeApiCall(ioDispatcher){
            service.getCollectionsListById(id).body()?.map {
                it.toDomainCollectionDetailList()
            }
        }
    }
}