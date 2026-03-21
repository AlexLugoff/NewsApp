package com.example.newsapp.presentation

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.MediumTest
import com.example.newsapp.di.module.RepositoryModule
import com.example.newsapp.domain.models.NewsSourceItem
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(RepositoryModule::class)
@MediumTest
class SourceSelectionBottomSheetFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val repository: NewsRepository = mockk(relaxed = true)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun displaySources_fromRepository() = runTest {
        // Given
        val mockSources = listOf(
            NewsSourceItem(1, "Habr", "link", true),
            NewsSourceItem(2, "Lenta", "link", false)
        )
        // Настраиваем мок репозитория, который использует GetNewsSourcesFlowUseCase
        coEvery { repository.getNewsSources() } returns flowOf(mockSources)

        // When
        launchFragmentInHiltContainer<SourceSelectionBottomSheetFragment>()

        // Then
        onView(withText("Habr")).check(matches(isDisplayed()))
        onView(withText("Lenta")).check(matches(isDisplayed()))
    }

    @Test
    fun clickingSource_triggersToggle() = runTest {
        // Given
        val source = NewsSourceItem(1, "Habr", "link", true)
        coEvery { repository.getNewsSources() } returns flowOf(listOf(source))

        // When
        launchFragmentInHiltContainer<SourceSelectionBottomSheetFragment>()

        // Кликаем по элементу (в адаптере вызовется viewModel.toggleSource)
        onView(withText("Habr")).perform(click())

        // Then: Проверяем, что репозиторий получил команду на переключение (id=1, isEnabled станет false)
        coVerify { repository.toggleSource(1, false) }
    }

    @Test
    fun dismiss_setsFragmentResult() = runTest {
        // Given
        coEvery { repository.getNewsSources() } returns flowOf(emptyList())
        var resultBundle: Bundle? = null

        launchFragmentInHiltContainer<SourceSelectionBottomSheetFragment> {
            parentFragmentManager.setFragmentResultListener("sources_updated", viewLifecycleOwner) { _, bundle ->
                resultBundle = bundle
            }

            (this as SourceSelectionBottomSheetFragment).dismiss()
        }

        // Then: Проверяем, что результат был получен
        // После завершения блока launch... фрагмент уничтожается, но результат в Bundle должен остаться
        assertNotNull("Result bundle should not be null", resultBundle)
        assertTrue(
            "isChanged should be true in fragment result",
            resultBundle?.getBoolean("isChanged") == true
        )
    }
}