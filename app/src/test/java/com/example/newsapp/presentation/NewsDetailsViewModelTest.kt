package com.example.newsapp.presentation

import androidx.lifecycle.SavedStateHandle
import com.example.newsapp.SealedResult
import com.example.newsapp.data.exception.DataError
import com.example.newsapp.domain.models.NewsItem
import com.example.newsapp.domain.usecases.GetNewsDetailsUseCase
import com.example.newsapp.ui.news_details.NewsDetailsViewModel
import com.example.newsapp.ui.news_details.NewsDetailsViewState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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
class NewsDetailsViewModelTest {

    private val mockUseCase = mockk<GetNewsDetailsUseCase>()
    private lateinit var viewModel: NewsDetailsViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    private val fakeNewsItem = NewsItem("link", "Title", "Desc", null, "link", 1L)
    private val fakeNewsLink = "link"

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `init should start with Loading state and transition to Success on Success result`() =
        runTest {
            // GIVEN
            val savedStateHandle = SavedStateHandle(mapOf("newsLink" to fakeNewsLink))
            // Use Case возвращает Success
            coEvery { mockUseCase.invoke(fakeNewsLink) } returns SealedResult.Success(fakeNewsItem)

            // WHEN
            viewModel = NewsDetailsViewModel(mockUseCase, savedStateHandle)

            // THEN
            assertEquals(NewsDetailsViewState.Loading, viewModel.uiState.first())
            advanceUntilIdle()
            assertEquals(NewsDetailsViewState.Success(fakeNewsItem), viewModel.uiState.value)
        }

    @Test
    fun `init should transition to Error state on Local NOT_FOUND Failure result`() = runTest {
        // GIVEN
        val savedStateHandle = SavedStateHandle(mapOf("newsLink" to fakeNewsLink))
        // Use Case возвращает Failure с доменной ошибкой
        coEvery { mockUseCase.invoke(fakeNewsLink) } returns SealedResult.Failure(DataError.Local.NOT_FOUND)

        // WHEN
        viewModel = NewsDetailsViewModel(mockUseCase, savedStateHandle)

        // THEN
        assertEquals(NewsDetailsViewState.Loading, viewModel.uiState.first())
        advanceUntilIdle()
        // Проверяем, что ошибка корректно смаплена
        assertEquals(
            NewsDetailsViewState.Error("Новость не найдена в кэше."),
            viewModel.uiState.value
        )
    }

    @Test
    fun `init should transition to Error state on unknown Failure result`() = runTest {
        // GIVEN
        val savedStateHandle = SavedStateHandle(mapOf("newsLink" to fakeNewsLink))
        // Имитируем другую (необработанную явно) ошибку
        val unknownError = DataError.Local.UNKNOWN
        coEvery { mockUseCase.invoke(fakeNewsLink) } returns SealedResult.Failure(unknownError)

        // WHEN
        viewModel = NewsDetailsViewModel(mockUseCase, savedStateHandle)

        // THEN
        advanceUntilIdle()
        // Проверяем, что сообщение об ошибке корректно смаплено
        assertEquals(
            NewsDetailsViewState.Error("Неизвестная ошибка: $unknownError"),
            viewModel.uiState.value
        )
    }

    // Внимание: Кейс, где UseCase возвращает Success(null), теперь должен быть
    // предотвращен в Data Layer, который должен возвращать Failure(NOT_FOUND)
    // вместо Success(null).

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}