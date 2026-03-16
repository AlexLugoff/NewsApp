package com.example.newsapp.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.newsapp.BaseUnitTest
import com.example.newsapp.data.exception.DataError
import com.example.newsapp.domain.models.NewsItem
import com.example.newsapp.domain.usecases.GetNewsDetailsUseCase
import com.example.newsapp.extensions.SealedResult
import com.example.newsapp.extensions.toSpannedHtml
import com.example.newsapp.presentation.news_details.NewsDetailsViewModel
import com.example.newsapp.presentation.news_details.NewsDetailsViewState
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NewsDetailsViewModelTest : BaseUnitTest() {

    @MockK
    lateinit var getNewsDetailsUseCase: GetNewsDetailsUseCase

    private lateinit var viewModel: NewsDetailsViewModel
    private val testLink = "https://example.com/news/123"

    @Test
    fun `init - when data exists - emits Success state`() = runTest {
        // Given: Мокаем успешный возврат данных
        val mockNews = NewsItem(
            id = "1",
            link = testLink,
            title = "Test Title",
            description = "Test Description".toSpannedHtml(),
            imageUrl = null,
            formattedDate = "10:00"
        )
        coEvery { getNewsDetailsUseCase(testLink) } returns SealedResult.Success(mockNews)

        // Подготавливаем SavedStateHandle с нужным аргументом
        val savedStateHandle = SavedStateHandle(mapOf("newsLink" to testLink))

        // When: Создаем ViewModel (триггерит init)
        viewModel = NewsDetailsViewModel(getNewsDetailsUseCase, savedStateHandle)

        // Then: Проверяем поток состояний
        viewModel.uiStateFlow.test {
            // 1. Сначала всегда идет Loading (initialValue)
            assertEquals(NewsDetailsViewState.Loading, awaitItem())

            // 2. Затем Success с нашими данными
            val state = awaitItem()
            assertTrue(state is NewsDetailsViewState.Success)
            assertEquals(mockNews, (state as NewsDetailsViewState.Success).newsItem)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `init - when use case fails - emits Error state`() = runTest {
        // Given: Имитируем ошибку
        val domainError = DataError.Local.NOT_FOUND
        coEvery { getNewsDetailsUseCase(testLink) } returns SealedResult.Failure(domainError)

        val savedStateHandle = SavedStateHandle(mapOf("newsLink" to testLink))

        // When
        viewModel = NewsDetailsViewModel(getNewsDetailsUseCase, savedStateHandle)

        // Then
        viewModel.uiStateFlow.test {
            assertEquals(NewsDetailsViewState.Loading, awaitItem())

            val state = awaitItem()
            assertTrue(state is NewsDetailsViewState.Error)
            // Здесь можно дополнительно проверить текст ошибки, если маппинг важен
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = IllegalStateException::class)
    fun `init - when newsLink is missing - throws exception`() = runTest {
        // Given: Пустой SavedStateHandle (без ключа "newsLink")
        val savedStateHandle = SavedStateHandle()

        // When: Это должно вызвать checkNotNull и выбросить исключение
        viewModel = NewsDetailsViewModel(getNewsDetailsUseCase, savedStateHandle)
    }
}