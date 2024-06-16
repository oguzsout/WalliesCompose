package com.oguzdogdu.walliescompose.favorites

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.oguzdogdu.walliescompose.domain.model.favorites.FavoriteImages
import com.oguzdogdu.walliescompose.domain.repository.WallpaperRepository
import com.oguzdogdu.walliescompose.domain.wrapper.Resource
import com.oguzdogdu.walliescompose.features.favorites.FavoritesViewModel
import com.oguzdogdu.walliescompose.features.favorites.event.FavoriteScreenEvent
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class FavoritesViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var repository: WallpaperRepository
    private lateinit var viewModel: FavoritesViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = FavoritesViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchImagesToFavorites success`() = runTest {
        val favoriteList = listOf(FavoriteImages(id = "0", url = null, profileImage = null, name = "Helloo"))
        val flow = flowOf(Resource.Success(favoriteList))
        `when`(repository.getFavorites()).thenReturn(flow)

        viewModel.handleUIEvent(FavoriteScreenEvent.GetFavorites)

        advanceUntilIdle()

        val state = viewModel.favoritesState.value
        assert(state.favorites == favoriteList)
        assert(!state.loading)
        assert(state.error == "")
    }

    @Test
    fun `fetchImagesToFavorites loading`() = runTest {
        val flow = flow {
            emit(Resource.Loading)
        }
        `when`(repository.getFavorites()).thenReturn(flow)

        viewModel.handleUIEvent(FavoriteScreenEvent.GetFavorites)

        advanceUntilIdle()

        assert(viewModel.favoritesState.value.loading)
    }

    @Test
    fun `fetchImagesToFavorites failure`() = runTest {
        val error = Throwable("Error fetching favorites")
        val flow = flow {
            emit(Resource.Error(error.message.orEmpty()))
        }
        `when`(repository.getFavorites()).thenReturn(flow)

        viewModel.handleUIEvent(FavoriteScreenEvent.GetFavorites)

        advanceUntilIdle()

        assert(viewModel.favoritesState.value.error == error.message)
    }

    @Test
    fun `deleteWithIdFromFavorites calls repository`() = runTest {
        val favoriteId = "some_id"

        viewModel.handleUIEvent(FavoriteScreenEvent.DeleteFromFavorites(favoriteId))

        advanceUntilIdle()

        verify(repository).deleteSpecificIdFavorite(favoriteId)
    }

    @Test
    fun `resetToFlipCardState changes state`() {
        viewModel.resetToFlipCardState(true)
        assert(viewModel.flipImageCard.value)

        viewModel.resetToFlipCardState(false)
        assert(!viewModel.flipImageCard.value)
    }
}