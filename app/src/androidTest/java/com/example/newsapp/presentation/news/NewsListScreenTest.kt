package com.example.newsapp.presentation.news

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.composetraining.ui.theme.NewsTheme
import com.example.newsapp.R
import com.example.newsapp.di.module.RepositoryModule
import com.example.newsapp.domain.models.NewsItem
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.extensions.toSpannedHtml
import com.example.newsapp.presentation.main.MainActivity
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@UninstallModules(RepositoryModule::class)
@RunWith(AndroidJUnit4::class)
class NewsListScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @BindValue
    @JvmField
    val repository: NewsRepository = mockk(relaxed = true)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun newsList_displaysItemsFromRepository() {
        // Given
        val mockNews = listOf(
            NewsItem(
                id = "1",
                link = "https://test.com/1",
                title = "Первая важная новость",
                description = "Описание первой новости".toSpannedHtml(),
                imageUrl = null,
                formattedDate = "10:00"
            )
        )
        coEvery { repository.getNewsFlow() } returns flowOf(mockNews)

        // When
        composeTestRule.setContent {
            NewsTheme {
                NewsListScreen(onNavigateToDetails = {})
            }
        }

        // Then
        composeTestRule.onNodeWithText("Первая важная новость").assertIsDisplayed()
    }

    @Test
    fun newsList_itemClick_triggersNavigation() {
        // Given
        val mockNews = listOf(
            NewsItem(
                id = "1",
                link = "https://test.com/1",
                title = "Кликабельная новость",
                description = "Описание".toSpannedHtml(),
                imageUrl = null,
                formattedDate = "10:00"
            )
        )
        coEvery { repository.getNewsFlow() } returns flowOf(mockNews)
        val onNavigateMock = mockk<(String) -> Unit>(relaxed = true)

        // When
        composeTestRule.setContent {
            NewsTheme {
                NewsListScreen(onNavigateToDetails = onNavigateMock)
            }
        }
        composeTestRule.onNodeWithText("Кликабельная новость").performClick()

        // Then
        verify { onNavigateMock("https://test.com/1") }
    }

    @Test
    fun newsList_settingsClick_opensSourceSelection() {
        // Given
        coEvery { repository.getNewsFlow() } returns flowOf(emptyList())
        coEvery { repository.getNewsSources() } returns flowOf(emptyList())

        // When
        composeTestRule.setContent {
            NewsTheme {
                NewsListScreen(onNavigateToDetails = {})
            }
        }

        // Кликаем по иконке настроек (используем contentDescription из NewsListScreen.kt)
        composeTestRule.onNodeWithContentDescription("Выбор источников").performClick()

        // Then: Проверяем, что заголовок BottomSheet появился
        val sheetTitle = composeTestRule.activity.getString(R.string.news_sources_title)
        composeTestRule.onNodeWithText(sheetTitle).assertIsDisplayed()
    }

    @Test
    fun newsList_emptyState_displaysMessage() {
        // Given
        coEvery { repository.getNewsFlow() } returns flowOf(emptyList())

        // When
        composeTestRule.setContent {
            NewsTheme {
                NewsListScreen(onNavigateToDetails = {})
            }
        }

        // Then
        val emptyMessage = composeTestRule.activity.getString(R.string.empty_list_message)
        composeTestRule.onNodeWithText(emptyMessage).assertIsDisplayed()
    }
}
