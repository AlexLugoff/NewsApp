package com.example.newsapp.presentation

import com.example.newsapp.SealedResult
import com.example.newsapp.data.exception.DataError
import com.example.newsapp.domain.models.NewsItem
import com.example.newsapp.domain.usecases.GetNewsListUseCase
import com.example.newsapp.ui.news.NewsListViewModel
import com.example.newsapp.ui.news.NewsListViewState
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

    private val mockUseCase = mockk<GetNewsListUseCase>()
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
        assertEquals(NewsListViewState.Success(fakeNews), viewModel.uiState.value)
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
        val finalState = viewModel.uiState.value
        assertEquals(
            NewsListViewState.Error("Ошибка сети: нет подключения к интернету."),
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
        val finalState = viewModel.uiState.value
        assertEquals(NewsListViewState.Error("Данные не найдены, даже в кэше."), finalState)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}