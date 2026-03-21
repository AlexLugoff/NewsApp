package com.example.newsapp.presentation

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.MediumTest
import com.example.newsapp.R
import com.example.newsapp.di.module.RepositoryModule
import com.example.newsapp.domain.models.NewsItem
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.extensions.toSpannedHtml
import com.example.newsapp.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(RepositoryModule::class) // Отключаем реальный репозиторий
@MediumTest
class NewsListFragmentTest {

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
    fun newsList_displaysItemsFromRepository() {
        // Given: Готовим моковые данные
        val mockNews = listOf(
            NewsItem(
                id = "1",
                link = "https://test.com/1",
                title = "Первая важная новость",
                description = "Описание первой новости".toSpannedHtml(),
                imageUrl = null,
                formattedDate = "10:00"
            ),
            NewsItem(
                id = "2",
                link = "https://test.com/2",
                title = "Вторая новость",
                description = "Описание второй новости".toSpannedHtml(),
                imageUrl = null,
                formattedDate = "11:00"
            )
        )

        // Настраиваем мок репозитория
        coEvery { repository.getNewsFlow() } returns flowOf(mockNews)

        // When: Запускаем фрагмент
        launchFragmentInHiltContainer<NewsListFragment>()

        // Then: Проверяем с помощью Espresso, что текст отобразился на экране
        onView(withText("Первая важная новость"))
            .check(matches(isDisplayed()))

        onView(withText("Вторая новость"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun newsList_emptyState_displaysMessage() {
        // Given: Репозиторий возвращает пустой список
        coEvery { repository.getNewsFlow() } returns flowOf(emptyList())

        // When
        launchFragmentInHiltContainer<NewsListFragment>()

        // Then: Проверяем наличие сообщения о пустом списке
        onView(withId(R.id.errorStatusTextView))
            .check(matches(isDisplayed()))
    }
}