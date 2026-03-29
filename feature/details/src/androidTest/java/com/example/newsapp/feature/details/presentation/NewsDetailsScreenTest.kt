package com.example.newsapp.feature.details.presentation

import android.content.Intent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.newsapp.core.common.result.SealedResult
import com.example.newsapp.core.domain.repository.NewsRepository
import com.example.newsapp.core.model.NewsItem
import com.example.newsapp.core.ui.theme.NewsTheme
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewsDetailsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val repository: NewsRepository = mockk(relaxed = true)

    @Test
    fun displayNewsDetails_successState() {
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

        composeTestRule.setContent {
            NewsTheme {
                NewsDetailsScreen(
                    newsLink = testLink,
                    onBackClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Заголовок новости").assertIsDisplayed()
        composeTestRule.onNodeWithText("Подробное описание новости").assertIsDisplayed()
        composeTestRule.onNodeWithText("16 марта, 12:00").assertIsDisplayed()
    }

    @Test
    fun shareClick_triggersShareIntent() {
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

            composeTestRule.onNodeWithContentDescription("Поделиться").performClick()

            intended(hasAction(Intent.ACTION_CHOOSER))
        } finally {
            Intents.release()
        }
    }
}
