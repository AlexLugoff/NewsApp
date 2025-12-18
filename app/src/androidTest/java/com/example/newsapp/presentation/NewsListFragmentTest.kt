package com.example.newsapp.presentation

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.newsapp.R
import com.example.newsapp.SealedResult
import com.example.newsapp.data.exception.DataError
import com.example.newsapp.di.TestRepositoryModule
import com.example.newsapp.domain.models.NewsItem
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.ui.news.NewsListFragment
import com.example.newsapp.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.clearMocks
import io.mockk.coEvery
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(TestRepositoryModule::class) // Снова указываем, что подменяем
@RunWith(AndroidJUnit4::class)
class NewsListFragmentTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var mockRepository: NewsRepository

    private val fakeNewsList = listOf(
        NewsItem(
            "id1",
            "Заголовок новости 1",
            "Описание 1",
            "http://image.url/1",
            "http://link.url/1",
            1700000000000L
        )
    )

    @Before
    fun setup() {
        hiltRule.inject()
        clearMocks(mockRepository)
    }

    // --- Тестирование состояния SUCCESS ---
    @Test
    fun test_list_is_displayed_and_loading_is_gone_on_success() {
        // GIVEN: Мокируем успешный результат
        coEvery { mockRepository.getNews() } returns SealedResult.Success(fakeNewsList)

        // WHEN: Запускаем фрагмент в контейнере
        launchFragmentInHiltContainer<NewsListFragment>()

        // THEN: Проверяем, что список виден, а индикатор загрузки скрыт
        onView(withId(R.id.newsRecyclerView))
            .check(matches(isDisplayed()))

        onView(withText("Заголовок новости 1"))
            .check(matches(isDisplayed()))

        onView(withId(R.id.progressBar))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))

        onView(withId(R.id.errorStatusTextView))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    // --- Тестирование состояния ERROR ---
    @Test
    fun test_error_message_is_displayed_and_list_is_gone_on_failure() {
        // GIVEN: Мокируем ошибку сети
        val errorMessage = "Ошибка сети: нет подключения к интернету."
        coEvery { mockRepository.getNews() } returns SealedResult.Failure(DataError.Network.UNKNOWN_HOST)

        // WHEN
        launchFragmentInHiltContainer<NewsListFragment>()

        // THEN: Проверяем, что сообщение об ошибке видна
        onView(withId(R.id.errorStatusTextView))
            .check(matches(isDisplayed()))
            .check(matches(withText(errorMessage)))

        onView(withId(R.id.newsRecyclerView))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    // --- Тестирование состояния LOADING (первичная загрузка) ---
    @Test
    fun test_progress_bar_is_displayed_on_initial_loading() {
        // GIVEN: Мокируем бесконечную загрузку (не возвращаем результат)
        coEvery { mockRepository.getNews() } coAnswers { throw Exception("Test loading") }

        // WHEN
        launchFragmentInHiltContainer<NewsListFragment>()

        // THEN: Проверяем, что ProgressBar виден
        onView(withId(R.id.progressBar))
            .check(matches(isDisplayed()))

        onView(withId(R.id.newsRecyclerView))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }
}