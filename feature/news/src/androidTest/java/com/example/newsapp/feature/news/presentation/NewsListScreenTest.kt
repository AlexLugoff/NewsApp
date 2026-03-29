package com.example.newsapp.feature.news.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.newsapp.core.ui.theme.NewsTheme
import com.example.newsapp.core.domain.repository.NewsRepository
import com.example.newsapp.core.model.NewsItem
import com.example.newsapp.feature.news.R
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewsListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val repository: NewsRepository = mockk(relaxed = true)

    @Test
    fun newsList_displaysItems() {
        val mockNews = listOf(
            NewsItem(
                id = "1",
                link = "https://test.com/1",
                title = "Первая важная новость",
                description = "Описание первой новости",
                imageUrl = null,
                formattedDate = "10:00"
            )
        )
        coEvery { repository.getNewsFlow() } returns flowOf(mockNews)

        composeTestRule.setContent {
            NewsTheme {
                NewsListScreen(
                    onNavigateToDetails = {},
                    openSourceSelectionSheet = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Первая важная новость").assertIsDisplayed()
    }

    @Test
    fun newsList_itemClick_triggersNavigation() {
        val mockNews = listOf(
            NewsItem(
                id = "1",
                link = "https://test.com/1",
                title = "Кликабельная новость",
                description = "Описание",
                imageUrl = null,
                formattedDate = "10:00"
            )
        )
        coEvery { repository.getNewsFlow() } returns flowOf(mockNews)
        val onNavigateMock = mockk<(String) -> Unit>(relaxed = true)

        composeTestRule.setContent {
            NewsTheme {
                NewsListScreen(
                    onNavigateToDetails = onNavigateMock,
                    openSourceSelectionSheet = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Кликабельная новость").performClick()

        verify { onNavigateMock("https://test.com/1") }
    }
}
