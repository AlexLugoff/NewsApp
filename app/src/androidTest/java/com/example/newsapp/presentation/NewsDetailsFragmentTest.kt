package com.example.newsapp.presentation

import android.content.Intent
import androidx.core.os.bundleOf
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.MediumTest
import com.example.newsapp.R
import com.example.newsapp.data.exception.DataError
import com.example.newsapp.di.module.RepositoryModule
import com.example.newsapp.domain.models.NewsItem
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.extensions.SealedResult
import com.example.newsapp.extensions.toSpannedHtml
import com.example.newsapp.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.mockk
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(RepositoryModule::class)
@MediumTest
class NewsDetailsFragmentTest {

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
    fun displayNewsDetails_successState() {
        // Given: Готовим данные и аргументы
        val testLink = "https://test.com/news1"
        val fragmentArgs = bundleOf("newsLink" to testLink)

        val mockNews = NewsItem(
            id = "1",
            link = testLink,
            title = "Заголовок новости",
            description = "Подробное описание новости".toSpannedHtml(),
            imageUrl = "https://test.com/image.jpg",
            formattedDate = "16 марта, 12:00",
            category = "Категория"
        )

        coEvery { repository.getNewsDetails(testLink) } returns SealedResult.Success(mockNews)

        // When: Запускаем фрагмент с аргументами
        launchFragmentInHiltContainer<NewsDetailsFragment>(fragmentArgs)

        // Then: Проверяем отображение всех полей
        onView(withId(R.id.titleTextView)).check(matches(withText("Заголовок новости")))
        onView(withId(R.id.descriptionTextView)).check(matches(withText("Подробное описание новости")))
        onView(withId(R.id.dateTextView)).check(matches(withText("16 марта, 12:00")))

        // Проверяем видимость группы контента и скрытость прогресс-бара
        onView(withId(R.id.contentGroup)).check(matches(isDisplayed()))
        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
    }

    @Test
    fun errorState_showsToast() {
        // Given
        val testLink = "https://test.com/error"
        val fragmentArgs = bundleOf("newsLink" to testLink)

        coEvery { repository.getNewsDetails(testLink) } returns SealedResult.Failure(DataError.Local.NotFound())

        // When
        launchFragmentInHiltContainer<NewsDetailsFragment>(fragmentArgs)

        // Then: В твоем коде при ошибке вызывается CommonEvent.ShowLongToast.
        // Проверить сам Toast в Espresso можно, но часто проверяют, что контент скрыт
        onView(withId(R.id.contentGroup)).check(matches(not(isDisplayed())))
    }

    @Test
    fun clickingBrowserButton_triggersIntent() {
        // Given
        val testLink = "https://test.com/news1"
        val fragmentArgs = bundleOf("newsLink" to testLink)
        val mockNews = NewsItem(testLink, "Title", "Desc".toSpannedHtml(), null, testLink, "date", "Категория")

        coEvery { repository.getNewsDetails(testLink) } returns SealedResult.Success(mockNews)

        // Инициализируем Intents для проверки исходящих намерений
        Intents.init()

        try {
            // When
            launchFragmentInHiltContainer<NewsDetailsFragment>(fragmentArgs)
            onView(withId(R.id.openInBrowserButton)).perform(click())

            // Then: Проверяем, что был послан Intent на открытие ссылки
            intended(hasAction(Intent.ACTION_VIEW))
            intended(hasData(testLink))
        } finally {
            Intents.release()
        }
    }
}