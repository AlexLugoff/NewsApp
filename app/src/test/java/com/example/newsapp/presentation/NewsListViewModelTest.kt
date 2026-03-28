package com.example.newsapp.presentation

import app.cash.turbine.test
import com.example.newsapp.BaseUnitTest
import com.example.newsapp.core.common.error.DataError
import com.example.newsapp.core.common.result.SealedResult
import com.example.newsapp.core.domain.usecase.GetNewsFlowUseCase
import com.example.newsapp.core.domain.usecase.RefreshNewsUseCase
import com.example.newsapp.core.model.NewsItem
import com.example.newsapp.core.ui.util.toReadableText
import com.example.newsapp.presentation.news.NewsListViewModel
import com.example.newsapp.presentation.news.NewsListViewState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NewsListViewModelTest : BaseUnitTest() {

    @MockK
    lateinit var getNewsFlowUseCase: GetNewsFlowUseCase

    @MockK
    lateinit var refreshNewsUseCase: RefreshNewsUseCase

    private val dbFlow = MutableStateFlow<List<NewsItem>>(emptyList())

    @Before
    override fun setup() {
        super.setup()
        every { getNewsFlowUseCase() } returns dbFlow
    }

    @Test
    fun `init - starts refreshing news`() = runTest {
        coEvery { refreshNewsUseCase() } returns SealedResult.Success(Unit)

        NewsListViewModel(getNewsFlowUseCase, refreshNewsUseCase)

        coVerify { refreshNewsUseCase() }
    }

    @Test
    fun `uiStateFlow - initial state is Loading`() = runTest {
        coEvery { refreshNewsUseCase() } coAnswers {
            kotlinx.coroutines.delay(100)
            SealedResult.Success(Unit)
        }

        val viewModel = NewsListViewModel(getNewsFlowUseCase, refreshNewsUseCase)

        viewModel.uiStateFlow.test {
            assertEquals(NewsListViewState.Loading, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `uiStateFlow - success loading news from DB`() = runTest {
        val mockNews = listOf(
            NewsItem("1", "Title", "Description", null, "link", "date")
        )
        coEvery { refreshNewsUseCase() } returns SealedResult.Success(Unit)
        dbFlow.value = mockNews

        val viewModel = NewsListViewModel(getNewsFlowUseCase, refreshNewsUseCase)

        viewModel.uiStateFlow.test {
            val lastItem = expectMostRecentItem()
            assertTrue(lastItem is NewsListViewState.Success)
            assertEquals(mockNews, (lastItem as NewsListViewState.Success).news)
        }
    }

    @Test
    fun `uiStateFlow - error when DB empty and refresh fails`() = runTest {
        val networkError = DataError.Network.Unknown()
        coEvery { refreshNewsUseCase() } returns SealedResult.Failure(networkError)
        dbFlow.value = emptyList()

        val viewModel = NewsListViewModel(getNewsFlowUseCase, refreshNewsUseCase)

        viewModel.uiStateFlow.test {
            val lastItem = expectMostRecentItem()
            assertTrue(lastItem is NewsListViewState.Error)
            assertEquals(
                networkError.toReadableText(),
                (lastItem as NewsListViewState.Error).message
            )
        }
    }

    @Test
    fun `refreshNews - does not run if already refreshing`() = runTest {
        coEvery { refreshNewsUseCase() } coAnswers {
            kotlinx.coroutines.delay(1000)
            SealedResult.Success(Unit)
        }

        val viewModel = NewsListViewModel(getNewsFlowUseCase, refreshNewsUseCase)

        // Попытка вызвать второй раз
        viewModel.refreshNews()

        // Проверяем, что вызов был только один раз (из init)
        coVerify(exactly = 1) { refreshNewsUseCase() }
    }
}
