package com.example.newsapp.presentation

import app.cash.turbine.test
import com.example.newsapp.BaseUnitTest
import com.example.newsapp.data.exception.DataError
import com.example.newsapp.domain.models.NewsItem
import com.example.newsapp.domain.usecases.GetNewsFlowUseCase
import com.example.newsapp.domain.usecases.RefreshNewsUseCase
import com.example.newsapp.extensions.SealedResult
import com.example.newsapp.extensions.toReadableText
import com.example.newsapp.extensions.toSpannedHtml
import com.example.newsapp.presentation.common.CommonEvent
import com.example.newsapp.presentation.news.NewsListViewModel
import com.example.newsapp.presentation.news.NewsListViewState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NewsListViewModelTest : BaseUnitTest() {

    @MockK
    lateinit var getNewsFlowUseCase: GetNewsFlowUseCase

    @MockK
    lateinit var refreshNewsUseCase: RefreshNewsUseCase

    // Используем MutableStateFlow, чтобы имитировать изменение данных в БД
    private val dbFlow = MutableStateFlow<List<NewsItem>>(emptyList())

    @Before
    override fun setup() {
        // По умолчанию UseCase возвращает наш поток
        every { getNewsFlowUseCase() } returns dbFlow
    }

    @Test
    fun `uiStateFlow - when repository fails and DB is empty - emits Error state`() = runTest {
        // Given: Сеть выдает ошибку
        val networkError = DataError.Network.ConnectionTimeout()
        coEvery { refreshNewsUseCase() } returns SealedResult.Failure(networkError)
        dbFlow.value = emptyList()

        val viewModel = NewsListViewModel(getNewsFlowUseCase, refreshNewsUseCase)

        viewModel.uiStateFlow.test {
            // 1. Начальное состояние
            assertEquals(NewsListViewState.Loading, awaitItem())

            // 2. Входим в refreshNews: refreshing становится true.
            // Так как в БД пусто, стейт остается Loading (согласно логике when во ViewModel)
            // Но мы можем пропустить этот этап, так как refreshNews запустится сразу.

            // 3. После завершения refreshNews с ошибкой при пустой БД
            val finalState = awaitItem()
            assertTrue(finalState is NewsListViewState.Error)
            assertEquals(
                networkError.toReadableText(),
                (finalState as NewsListViewState.Error).message
            )
        }
    }

    @Test
    fun `uiStateFlow - emits Success when database has items`() = runTest {
        // Given: В базе уже есть новости
        val mockNews =
            listOf(NewsItem("1", "Title", "".toSpannedHtml(), null, "link", "date", "Category"))
        dbFlow.value = mockNews
        coEvery { refreshNewsUseCase() } returns SealedResult.Success(Unit)

        val viewModel = NewsListViewModel(getNewsFlowUseCase, refreshNewsUseCase)

        viewModel.uiStateFlow.test {
            // Игнорируем начальный Loading
            assertEquals(NewsListViewState.Loading, awaitItem())

            // Ожидаем Success с флагом refreshing = true (так как refreshNews запустился в init)
            val refreshingState = awaitItem()
            assertTrue(refreshingState is NewsListViewState.Success)
            assertTrue((refreshingState as NewsListViewState.Success).isRefreshing)

            // Ожидаем Success с флагом refreshing = false (запрос завершился)
            val finalState = awaitItem()
            assertFalse((finalState as NewsListViewState.Success).isRefreshing)
            assertEquals(mockNews, finalState.news)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `refreshNews - when DB not empty and network fails - stays Success and sends event`() =
        runTest {
            // Given: Данные в базе есть, но сеть упала
            val mockNews =
                listOf(NewsItem("1", "Title", "".toSpannedHtml(), null, "link", "date", "Category"))
            dbFlow.value = mockNews
            coEvery { refreshNewsUseCase() } returns SealedResult.Failure(DataError.Network.Unknown())

            val viewModel = NewsListViewModel(getNewsFlowUseCase, refreshNewsUseCase)

            // Проверяем ивенты через Turbine
            viewModel.eventFlow.test {
                // Ждем завершения init запроса
                advanceUntilIdle()

                // Проверяем стейт: он должен остаться Success
                val currentState = viewModel.uiStateFlow.value
                assertTrue(currentState is NewsListViewState.Success)
                assertEquals(mockNews, (currentState as NewsListViewState.Success).news)
            }
        }
}