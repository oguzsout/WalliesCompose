package com.oguzdogdu.walliescompose.data.repository

import com.oguzdogdu.walliescompose.data.model.topics.TopicsResponseItem
import com.oguzdogdu.walliescompose.data.model.topics.toDomainTopics
import com.oguzdogdu.walliescompose.domain.common.Resource
import com.oguzdogdu.walliescompose.domain.model.Topics
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class WallpaperRepositoryImplTest {

    @Mock
    private lateinit var mockWallpaperService: com.oguzdogdu.walliescompose.data.service.WallpaperService

    private lateinit var sut: WallpaperRepositoryImpl

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        sut = WallpaperRepositoryImpl(mockWallpaperService, testDispatcher)
    }

    @Test
    fun `getTopicsTitle_serviceReturnsSuccessfulResponse_emitsSuccess`() = runTest {
        val topicResponseItem = TopicsResponseItem(
            id = "1",
            title = "Nature",
            slug = "nature",
            description = "Nature photos",
            coverPhoto = TopicsResponseItem.CoverPhoto(TopicsResponseItem.Urls("url_regular")),
            previewPhotos = listOf(
                TopicsResponseItem.PreviewPhoto(TopicsResponseItem.Urls("url_thumb"))
            )
        )
        val expectedDomainTopic = topicResponseItem.toDomainTopics()
        val mockResponse = Response.success(listOf(topicResponseItem))

        `when`(mockWallpaperService.getTopics(perPage = 6, page = 1)).thenReturn(mockResponse)

        val emissions = sut.getTopicsTitle().toList()

        assertEquals(2, emissions.size)
        assertTrue(emissions[0] is Resource.Loading)
        assertTrue(emissions[1] is Resource.Success)
        assertEquals(listOf(expectedDomainTopic), (emissions[1] as Resource.Success).data)
    }

    @Test
    fun `getTopicsTitle_serviceReturnsHttpError_emitsError`() = runTest {
        val errorBodyString = "{\"errors\":[\"Not found\"]}"
        val errorBody = errorBodyString.toResponseBody(null)
        val mockResponse = Response.error<List<TopicsResponseItem>>(404, errorBody)

        `when`(mockWallpaperService.getTopics(perPage = 6, page = 1)).thenReturn(mockResponse)

        val emissions = sut.getTopicsTitle().toList()

        assertEquals(2, emissions.size)
        assertTrue(emissions[0] is Resource.Loading)
        assertTrue(emissions[1] is Resource.Error)
        assertEquals(errorBodyString, (emissions[1] as Resource.Error).errorMessage)
    }

    @Test
    fun `getTopicsTitle_serviceThrowsIOException_emitsError`() = runTest {
        val errorMessage = "Network error"
        `when`(mockWallpaperService.getTopics(perPage = 6, page = 1)).thenThrow(IOException(errorMessage))

        val emissions = sut.getTopicsTitle().toList()

        assertEquals(2, emissions.size)
        assertTrue(emissions[0] is Resource.Loading)
        assertTrue(emissions[1] is Resource.Error)
        assertEquals(errorMessage, (emissions[1] as Resource.Error).errorMessage)
    }
}
