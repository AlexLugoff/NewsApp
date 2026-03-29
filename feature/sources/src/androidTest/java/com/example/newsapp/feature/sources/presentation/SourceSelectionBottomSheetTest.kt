package com.example.newsapp.feature.sources.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.newsapp.core.ui.theme.NewsTheme
import com.example.newsapp.core.domain.repository.NewsRepository
import com.example.newsapp.core.model.NewsSourceItem
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SourceSelectionBottomSheetTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val repository: NewsRepository = mockk(relaxed = true)

    @Test
    fun displaySources_fromRepository() {
        val mockSources = listOf(
            NewsSourceItem(1, "Habr", "link", true),
            NewsSourceItem(2, "Lenta", "link", false)
        )
        coEvery { repository.getNewsSources() } returns flowOf(mockSources)

        composeTestRule.setContent {
            NewsTheme {
                SourceSelectionBottomSheetScreen(
                    onDismiss = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Habr").assertIsDisplayed()
        composeTestRule.onNodeWithText("Lenta").assertIsDisplayed()
    }

    @Test
    fun clickingSource_triggersToggle() {
        val source = NewsSourceItem(1, "Habr", "link", true)
        coEvery { repository.getNewsSources() } returns flowOf(listOf(source))

        composeTestRule.setContent {
            NewsTheme {
                SourceSelectionBottomSheetScreen(
                    onDismiss = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Habr").performClick()

        coVerify { repository.toggleSource(1, false) }
    }
}
