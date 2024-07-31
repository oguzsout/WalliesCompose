package com.oguzdogdu.walliescompose.data.service

import com.oguzdogdu.walliescompose.data.model.collection.CollectionResponse
import com.oguzdogdu.walliescompose.data.model.maindto.Photo
import com.oguzdogdu.walliescompose.data.model.maindto.UnsplashResponseItem
import com.oguzdogdu.walliescompose.data.model.photodetail.PhotoDetailResponse
import com.oguzdogdu.walliescompose.data.model.searchdto.SearchResponseItem
import com.oguzdogdu.walliescompose.data.model.topics.CoverPhoto
import com.oguzdogdu.walliescompose.data.model.topics.TopicsResponseItem

interface WallpaperService {

    suspend fun getImagesByOrders(
        perPage: Int?,
        page: Int?,
        order: String?,
    ): List<UnsplashResponseItem>

    suspend fun getPhoto(
        id: String?,
    ): PhotoDetailResponse

    suspend fun searchPhotos(
        page: Int?,
        perPage: Int?,
        query: String,
        language: String?,
    ): SearchResponseItem

    suspend fun getCollections(
        page: Int?,
        perPage: Int?
    ): List<CollectionResponse>

    suspend fun getCollectionsListById(
        id: String?,
    ): List<Photo>

    suspend fun getTopics(
        page: Int?,
        perPage: Int?,
    ): List<TopicsResponseItem>

    suspend fun getTopicList(
        id: String?,
        page: Int?,
        perPage: Int?,
    ): List<CoverPhoto>

    suspend fun getACollection(id: String?): CollectionResponse

    suspend fun getRandomPhotos(
        count: Int?
    ): List<UnsplashResponseItem>
}