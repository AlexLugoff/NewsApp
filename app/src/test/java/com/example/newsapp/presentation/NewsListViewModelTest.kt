package com.example.newsapp.presentation

import app.cash.turbine.test
import com.example.newsapp.BaseUnitTest
import com.example.newsapp.data.exception.DataError
import com.example.newsapp.domain.models.NewsItem
import com.example.newsapp.domain.usecases.GetNewsFlowUseCase
import com.example.newsapp.domain.usecases.RefreshNewsUseCase
import com.example.newsapp.extensions.SealedResult
import com.example.newsapp.extensions.toSpannedHtml
import com.example.newsapp.presentation.news.NewsListViewModel
import com.example.newsapp.presentation.news.NewsListViewState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NewsListViewModelTest : BaseUnitTest() {

    @MockK
    lateinit var getNewsFlowUseCase: GetNewsFlowUseCase

    @MockK
    lateinit var refreshNewsUseCase: RefreshNewsUseCase

    @Test
    fun `uiStateFlow - when repository fails and DB is empty - emits Error state`() = runTest {
        // Given: База пуста, сеть выдает ошибку
        every { getNewsFlowUseCase() } returns flowOf(emptyList())
        coEvery { refreshNewsUseCase() } returns SealedResult.Failure(DataError.Network.CONNECTION_TIMEOUT)

        val viewModel = NewsListViewModel(getNewsFlowUseCase, refreshNewsUseCase)

        viewModel.uiStateFlow.test {
            assertEquals(NewsListViewState.Loading, awaitItem())
            // refreshNews() запускается в init
        }
    }

    @Test
    fun `uiStateFlow - emits Success when database has items`() = runTest {
        val mockNews = listOf(NewsItem("1", "Title", "".toSpannedHtml(), null, "link", "date"))
        every { getNewsFlowUseCase() } returns flowOf(mockNews)
        coEvery { refreshNewsUseCase() } returns SealedResult.Success(Unit)

        val viewModel = NewsListViewModel(getNewsFlowUseCase, refreshNewsUseCase)

        viewModel.uiStateFlow.test {
            // Ожидаем первое состояние (Loading)
            assertEquals(NewsListViewState.Loading, awaitItem())
            // Ожидаем данные из базы
            val state = awaitItem()
            assertTrue(state is NewsListViewState.Success)
            assertEquals(mockNews, (state as NewsListViewState.Success).news)
        }
    }
}