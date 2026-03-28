package com.example.newsapp.presentation.news_details

import android.content.Intent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.composetraining.ui.theme.NewsTheme
import com.example.newsapp.R
import com.example.newsapp.core.common.SealedResult
import com.example.newsapp.core.data.di.RepositoryModule
import com.example.newsapp.core.domain.repository.NewsRepository
import com.example.newsapp.core.model.NewsItem
import com.example.newsapp.presentation.main.MainActivity
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@UninstallModules(RepositoryModule::class)
@RunWith(AndroidJUnit4::class)
class NewsDetailsScreenTest {

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
    fun displayNewsDetails_successState() {
        // Given
        val testLink = "https://test.com/news1"
        val mockNews = NewsItem(
            id = "1",
            link = testLink,
            title = "Заголовок новости",
            description = "Подробное описание новости",
            imageUrl = null,
            formattedDate = "16 марта, 12:00"
        )
        coEvery { repository.getNewsDetails(testLink) } returns SealedResult.Success(mockNews)

        // When
        composeTestRule.setContent {
            NewsTheme {
                NewsDetailsScreen(
                    newsLink = testLink,
                    onBackClick = {}
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Заголовок новости").assertIsDisplayed()
        composeTestRule.onNodeWithText("Подробное описание новости").assertIsDisplayed()
        composeTestRule.onNodeWithText("16 марта, 12:00").assertIsDisplayed()
    }

    @Test
    fun backClick_triggersOnBack() {
        // Given
        val testLink = "https://test.com/news1"
        val mockNews = NewsItem("1", "Title", "Desc", null, testLink, "date")
        coEvery { repository.getNewsDetails(testLink) } returns SealedResult.Success(mockNews)
        val onBackMock = mockk<() -> Unit>(relaxed = true)

        // When
        composeTestRule.setContent {
            NewsTheme {
                NewsDetailsScreen(
                    newsLink = testLink,
                    onBackClick = onBackMock
                )
            }
        }

        // Находим кнопку Back в TopAppBar по contentDescription (R.string.back)
        val backDesc = composeTestRule.activity.getString(R.string.back)
        composeTestRule.onNodeWithContentDescription(backDesc).performClick()

        // Then
        verify { onBackMock() }
    }

    @Test
    fun shareClick_triggersShareIntent() {
        // Given
        val testLink = "https://test.com/share"
        val mockNews = NewsItem("1", "Title", "Desc", null, testLink, "date")
        coEvery { repository.getNewsDetails(testLink) } returns SealedResult.Success(mockNews)

        Intents.init()
        try {
            composeTestRule.setContent {
                NewsTheme {
                    NewsDetailsScreen(testLink, {})
                }
            }

            // Находим кнопку Share в TopAppBar по contentDescription (R.string.share)
            val shareDesc = composeTestRule.activity.getString(R.string.share)
            composeTestRule.onNodeWithContentDescription(shareDesc).performClick()

            // Then
            intended(hasAction(Intent.ACTION_CHOOSER))
        } finally {
            Intents.release()
        }
    }
}
