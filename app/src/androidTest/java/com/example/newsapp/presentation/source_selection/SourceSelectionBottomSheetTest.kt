package com.example.newsapp.presentation.source_selection

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.composetraining.ui.theme.NewsTheme
import com.example.newsapp.core.model.NewsSourceItem
import com.example.newsapp.di.module.RepositoryModule
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.presentation.main.MainActivity
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@UninstallModules(RepositoryModule::class)
@RunWith(AndroidJUnit4::class)
class SourceSelectionBottomSheetTest {

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
    fun displaySources_fromRepository() {
        // Given
        val mockSources = listOf(
            NewsSourceItem(1, "Habr", "link", true),
            NewsSourceItem(2, "Lenta", "link", false)
        )
        coEvery { repository.getNewsSources() } returns flowOf(mockSources)

        // When
        composeTestRule.setContent {
            NewsTheme {
                SourceSelectionBottomSheetScreen(
                    onDismiss = {}
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Habr").assertIsDisplayed()
        composeTestRule.onNodeWithText("Lenta").assertIsDisplayed()
    }

    @Test
    fun clickingSource_triggersToggle() {
        // Given
        val source = NewsSourceItem(1, "Habr", "link", true)
        coEvery { repository.getNewsSources() } returns flowOf(listOf(source))

        // When
        composeTestRule.setContent {
            NewsTheme {
                SourceSelectionBottomSheetScreen(
                    onDismiss = {}
                )
            }
        }

        // Кликаем по названию источника
        composeTestRule.onNodeWithText("Habr").performClick()

        // Then: Проверяем, что репозиторий получил команду на переключение
        // (ViewModel вызывает toggleSource, который вызывает repository.toggleSource)
        coVerify { repository.toggleSource(1, false) }
    }
}
