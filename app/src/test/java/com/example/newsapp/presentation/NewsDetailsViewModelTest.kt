package com.example.newsapp.presentation

import app.cash.turbine.test
import com.example.newsapp.BaseUnitTest
import com.example.newsapp.core.model.NewsItem
import com.example.newsapp.data.exception.DataError
import com.example.newsapp.domain.usecases.GetNewsDetailsUseCase
import com.example.newsapp.extensions.SealedResult
import com.example.newsapp.presentation.news_details.NewsDetailsEvent
import com.example.newsapp.presentation.news_details.NewsDetailsViewModel
import com.example.newsapp.presentation.news_details.NewsDetailsViewState
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NewsDetailsViewModelTest : BaseUnitTest() {

    @MockK
    lateinit var getNewsDetailsUseCase: GetNewsDetailsUseCase

    private lateinit var viewModel: NewsDetailsViewModel
    private val testLink = "https://example.com/news/123"

    @Before
    override fun setup() {
        super.setup()
        viewModel = NewsDetailsViewModel(getNewsDetailsUseCase)
    }

    @Test
    fun `loadNewsDetails - when data exists - emits Success state`() = runTest {
        // Given
        val mockNews = NewsItem(
            id = "1",
            link = testLink,
            title = "Test Title",
            description = "Test Description",
            imageUrl = null,
            formattedDate = "10:00"
        )
        coEvery { getNewsDetailsUseCase(testLink) } returns SealedResult.Success(mockNews)

        // When
        viewModel.loadNewsDetails(testLink)

        // Then
        viewModel.uiStateFlow.test {
            // Начальное состояние Loading
            assertEquals(NewsDetailsViewState.Loading, awaitItem())
            
            val state = awaitItem()
            assertTrue(state is NewsDetailsViewState.Success)
            assertEquals(mockNews, (state as NewsDetailsViewState.Success).newsItem)
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadNewsDetails - when use case fails - emits Error state`() = runTest {
        // Given
        val domainError = DataError.Local.NotFound()
        coEvery { getNewsDetailsUseCase(testLink) } returns SealedResult.Failure(domainError)

        // When
        viewModel.loadNewsDetails(testLink)

        // Then
        viewModel.uiStateFlow.test {
            assertEquals(NewsDetailsViewState.Loading, awaitItem())

            val state = awaitItem()
            assertTrue(state is NewsDetailsViewState.Error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `goToBrowser - sends GoToBrowser event`() = runTest {
        viewModel.eventFlow.test {
            viewModel.goToBrowser(testLink)
            
            val event = awaitItem()
            assertTrue(event is NewsDetailsEvent.GoToBrowser)
            assertEquals(testLink, (event as NewsDetailsEvent.GoToBrowser).url)
        }
    }

    @Test
    fun `shareNews - sends ShareNews event`() = runTest {
        viewModel.eventFlow.test {
            viewModel.shareNews(testLink)
            
            val event = awaitItem()
            assertTrue(event is NewsDetailsEvent.ShareNews)
            assertEquals(testLink, (event as NewsDetailsEvent.ShareNews).url)
        }
    }
}
