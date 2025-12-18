package com.example.newsapp.presentation

import android.view.View
import androidx.core.os.bundleOf
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.newsapp.R
import com.example.newsapp.SealedResult
import com.example.newsapp.data.exception.DataError
import com.example.newsapp.domain.models.NewsItem
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.ui.news_details.NewsDetailsFragment
import com.example.newsapp.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.clearMocks
import io.mockk.coEvery
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NewsDetailsFragmentTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var mockRepository: NewsRepository

    private val TEST_LINK = "http://test.link/details/1"
    private val TEST_DATE = 1700000000000L

    private val fakeNewsItem = NewsItem(
        id = "id1",
        title = "Заголовок тестовой новости",
        description = "Это полное описание тестовой новости.",
        imageUrl = "http://image.url/test",
        link = TEST_LINK,
        date = TEST_DATE
    )

    @Before
    fun setup() {
        hiltRule.inject()
        clearMocks(mockRepository)
    }

    // --- Успешное состояние (SUCCESS) ---
    @Test
    fun test_success_state_displays_all_content() {
        // GIVEN
        coEvery { mockRepository.getNewsDetails(TEST_LINK) } returns SealedResult.Success(
            fakeNewsItem
        )
        val fragmentArgs = bundleOf("newsLink" to TEST_LINK)

        // WHEN
        launchFragmentInHiltContainer<NewsDetailsFragment>(fragmentArgs)

        // THEN: Проверяем, что контент-группа видна и содержит правильный текст
        onView(withId(R.id.contentGroup))
            .check(matches(isDisplayed()))

        onView(withId(R.id.titleTextView))
            .check(matches(withText("Заголовок тестовой новости")))

        onView(withId(R.id.progressBar))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    // --- Состояние Ошибки (NOT_FOUND) ---
    @Test
    fun test_error_state_hides_content_and_shows_toast_on_not_found_failure() {
        // GIVEN
        coEvery { mockRepository.getNewsDetails(TEST_LINK) } returns SealedResult.Failure(DataError.Local.NOT_FOUND)
        val fragmentArgs = bundleOf("newsLink" to TEST_LINK)
        val expectedToastMessage = "Новость не найдена в кэше." // Текст из NewsDetailsViewModel

        // WHEN: Запускаем фрагмент
        var activityDecorView: View? = null
        launchFragmentInHiltContainer<NewsDetailsFragment>(fragmentArgs) {
            activityDecorView = activity?.window?.decorView
        }

        // THEN:

        // 1. Проверяем, что Toast появился с правильным сообщением
        onView(withText(expectedToastMessage))
            .inRoot(withDecorView(not(activityDecorView))) // Ищем Toast вне View иерархии Activity
            .check(matches(isDisplayed()))

        // 2. Проверяем, что контент скрыт (только ProgressBar и Toast должны быть видны)
        onView(withId(R.id.contentGroup))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))

        onView(withId(R.id.progressBar))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }
}