package com.example.newsapp.presentation

import com.example.newsapp.R
import com.example.newsapp.extensions.SealedResult
import com.example.newsapp.data.exception.DataError
import com.example.newsapp.domain.models.NewsItem
import com.example.newsapp.domain.usecases.RefreshNewsUseCase
import com.example.newsapp.presentation.news.NewsListViewModel
import com.example.newsapp.presentation.news.NewsListViewState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class NewsListViewModelTest {

    private val mockUseCase = mockk<RefreshNewsUseCase>()

    private lateinit var viewModel: NewsListViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    private val fakeNews = listOf(NewsItem("1", "Title 1", "Desc 1", null, "link1", 1L))

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `init should transition to Success state on use case Success`() = runTest {
        // GIVEN
        coEvery { mockUseCase.invoke() } returns SealedResult.Success(fakeNews)

        // WHEN
        viewModel = NewsListViewModel(mockUseCase)
        advanceUntilIdle()

        // THEN
        assertEquals(NewsListViewState.Success(fakeNews), viewModel.viewState.value)
    }

    @Test
    fun `loadNews should transition to Error state and map Network Error message`() = runTest {
        // GIVEN: UseCase возвращает ошибку сети
        coEvery { mockUseCase.invoke() } returns SealedResult.Failure(DataError.Network.UNKNOWN_HOST)
        viewModel = NewsListViewModel(mockUseCase)

        // WHEN: Вызываем loadNews
        viewModel.loadNews()
        advanceUntilIdle()

        // THEN: Проверяем, что сообщение об ошибке корректно смаплено
        val finalState = viewModel.viewState.value
        assertEquals(
            NewsListViewState.Error(UniversalText.Resource(id = R.string.error_message_no_network_connection)),
            finalState
        )
    }

    @Test
    fun `loadNews should transition to Error state and map Local Error message`() = runTest {
        // GIVEN: UseCase возвращает ошибку, что данных нет
        coEvery { mockUseCase.invoke() } returns SealedResult.Failure(DataError.Local.NOT_FOUND)
        viewModel = NewsListViewModel(mockUseCase)

        // WHEN
        viewModel.loadNews()
        advanceUntilIdle()

        // THEN
        val finalState = viewModel.viewState.value

        assertEquals(NewsListViewState.Error(UniversalText.Resource(id = R.string.news_was_not_found_in_the_cache)), finalState)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}