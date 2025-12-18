package com.example.newsapp.domain

import com.example.newsapp.domain.models.NewsItem
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.domain.usecases.GetNewsDetailsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetNewsDetailsUseCaseTest {
    private val mockRepository = mockk<NewsRepository>()
    private lateinit var useCase: GetNewsDetailsUseCase

    private val fakeNewsItem = NewsItem("1", "Title", "Desc", null, "link", 1L)

    @Before
    fun setup() {
        useCase = GetNewsDetailsUseCase(mockRepository)
    }

    @Test
    fun `invoke should return success with NewsItem when found`() = runTest {
        // GIVEN
        val newsLink = "test_link"
        coEvery { mockRepository.getNewsDetails(newsLink) } returns fakeNewsItem

        // WHEN
        val result = useCase.invoke(newsLink)

        // THEN
        assertTrue(result.isSuccess)
        assertEquals(fakeNewsItem, result.getOrThrow())
    }

    @Test
    fun `invoke should return success with null when not found`() = runTest {
        // GIVEN
        val newsLink = "non_existent_link"
        coEvery { mockRepository.getNewsDetails(newsLink) } returns null

        // WHEN
        val result = useCase.invoke(newsLink)

        // THEN
        assertTrue(result.isSuccess)
        assertNull(result.getOrThrow())
    }

    @Test
    fun `invoke should return failure when repository throws exception`() = runTest {
        // GIVEN
        val newsLink = "test_link"
        val expectedError = RuntimeException("DB Error")
        coEvery { mockRepository.getNewsDetails(newsLink) } throws expectedError

        // WHEN
        val result = useCase.invoke(newsLink)

        // THEN
        assertTrue(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }
}