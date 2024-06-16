package com.oguzdogdu.walliescompose.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.DifferCallback
import androidx.paging.NullPaddedList
import androidx.paging.PagingData
import androidx.paging.PagingDataDiffer
import com.oguzdogdu.walliescompose.domain.model.search.SearchPhoto
import com.oguzdogdu.walliescompose.domain.model.search.searchuser.SearchUser
import com.oguzdogdu.walliescompose.domain.repository.AppSettingsRepository
import com.oguzdogdu.walliescompose.domain.repository.UnsplashUserRepository
import com.oguzdogdu.walliescompose.domain.repository.WallpaperRepository
import com.oguzdogdu.walliescompose.features.search.SearchEvent
import com.oguzdogdu.walliescompose.features.search.SearchViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var wallpaperRepository: WallpaperRepository
    private lateinit var unsplashUserRepository: UnsplashUserRepository
    private lateinit var appRepository: AppSettingsRepository
    private lateinit var viewModel: SearchViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        wallpaperRepository = mockk()
        unsplashUserRepository = mockk()
        appRepository = mockk()
        viewModel = SearchViewModel(wallpaperRepository, unsplashUserRepository, appRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `handleUIEvent updates searchListState and searchUserListState`() = runTest {
        val query = "test"
        val language = "en"
        val photos = PagingData.empty<SearchPhoto>()
        val users = PagingData.empty<SearchUser>()
        val listOfPhotos = listOf<SearchPhoto>()
        val listOfUsers = listOf<SearchUser>()

        coEvery { wallpaperRepository.searchPhoto(query, language) } returns flowOf(photos)
        coEvery { unsplashUserRepository.getSearchFromUsers(query) } returns flowOf(users)

        viewModel.handleUIEvent(SearchEvent.EnteredSearchQuery(query, language))

        val collectedPhotos = viewModel.searchListState.first().collectDataForTest()
        val collectedUsers = viewModel.searchUserListState.first().collectDataForTest()

        assertEquals(listOfPhotos, collectedPhotos)
        assertEquals(listOfUsers, collectedUsers)
    }

    @Test
    fun `handleUIEvent with empty query does not update states`() = runTest {
        val query = ""
        val language = "en"
        val photos = PagingData.empty<SearchPhoto>()
        val users = PagingData.empty<SearchUser>()

        coEvery { wallpaperRepository.searchPhoto(query, language) } returns flowOf(photos)
        coEvery { unsplashUserRepository.getSearchFromUsers(query) } returns flowOf(users)

        viewModel.handleUIEvent(SearchEvent.EnteredSearchQuery(query, language))

        val collectedPhotos = viewModel.searchListState.first().collectDataForTest()
        val collectedUsers = viewModel.searchUserListState.first().collectDataForTest()

        assertTrue(collectedPhotos.isEmpty())
        assertTrue(collectedUsers.isEmpty())
    }

    @Test
    fun `handleUIEvent with valid query updates searchListState and searchUserListState with non-empty data`() = runTest {
        val query = "test"
        val language = "en"
        val photos = PagingData.from(
            listOf(
                SearchPhoto(id = "1", url = "url1", imageDesc = null),
                SearchPhoto(id = "2", url = "url2", imageDesc = null)
            )
        )
        val users = PagingData.from(
            listOf(
                SearchUser(
                    id = "1",
                    name = "user1",
                    profileImage = null,
                    username = null
                ), SearchUser(id = "2", name = "user2", profileImage = null, username = null)
            )
        )
        val listOfPhotos = listOf(
            SearchPhoto(id = "1", url = "url1", imageDesc = null),
            SearchPhoto(id = "2", url = "url2", imageDesc = null)
        )
        val listOfUsers = listOf(
            SearchUser(id = "1", name = "user1", profileImage = null, username = null),
            SearchUser(id = "2", name = "user2", profileImage = null, username = null)
        )

        coEvery { wallpaperRepository.searchPhoto(query, language) } returns flowOf(photos)
        coEvery { unsplashUserRepository.getSearchFromUsers(query) } returns flowOf(users)

        viewModel.handleUIEvent(SearchEvent.EnteredSearchQuery(query, language))

        val collectedPhotos = viewModel.searchListState.first().collectDataForTest()
        val collectedUsers = viewModel.searchUserListState.first().collectDataForTest()

        assertEquals(listOfPhotos, collectedPhotos)
        assertEquals(listOfUsers, collectedUsers)
    }

    @Test
    fun `handleUIEvent GetAppLanguageValue updates appLanguage`() = runTest {
        val language = "en"
        coEvery { appRepository.getLanguageStrings("language") } returns flowOf(language)

        viewModel.handleUIEvent(SearchEvent.GetAppLanguageValue)

        val collectedLanguage = viewModel.appLanguage.first()
        assertEquals(language, collectedLanguage)
    }

    private suspend fun <T : Any> PagingData<T>.collectDataForTest(): List<T> {
        val dcb = object : DifferCallback {
            override fun onChanged(position: Int, count: Int) {}
            override fun onInserted(position: Int, count: Int) {}
            override fun onRemoved(position: Int, count: Int) {}
        }
        val items = mutableListOf<T>()
        val dif = object : PagingDataDiffer<T>(dcb, UnconfinedTestDispatcher()) {
            override suspend fun presentNewList(
                previousList: NullPaddedList<T>,
                newList: NullPaddedList<T>,
                lastAccessedIndex: Int,
                onListPresentable: () -> Unit
            ): Int? {
                for (id in 0 until newList.size) {
                    items.add(newList.getFromStorage(id))
                }
                onListPresentable()
                return null
            }
        }
        dif.collectFrom(this)
        return items
    }
}