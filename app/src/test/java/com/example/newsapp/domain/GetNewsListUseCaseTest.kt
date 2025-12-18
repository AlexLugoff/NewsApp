package com.example.newsapp.domain

import com.example.newsapp.SealedResult
import com.example.newsapp.data.exception.DataError
import com.example.newsapp.domain.models.NewsItem
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.domain.usecases.GetNewsListUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetNewsListUseCaseTest {

    private val mockRepository = mockk<NewsRepository>()
    private lateinit var useCase: GetNewsListUseCase

    private val fakeNews = listOf(
        NewsItem("1", "Title 1", "Desc 1", null, "link1", 1L)
    )
    private val fakeError = DataError.Network.UNKNOWN_HOST

    @Before
    fun setup() {
        useCase = GetNewsListUseCase(mockRepository)
    }

    @Test
    fun `invoke should return Success result when repository succeeds`() = runTest {
        // GIVEN: Репозиторий возвращает успех
        coEvery { mockRepository.getNews() } returns SealedResult.Success(fakeNews)

        // WHEN
        val result = useCase.invoke()

        // THEN
        assertEquals(SealedResult.Success(fakeNews), result)
    }

    @Test
    fun `invoke should return Failure result when repository fails`() = runTest {
        // GIVEN: Репозиторий возвращает ошибку
        coEvery { mockRepository.getNews() } returns SealedResult.Failure(fakeError)

        // WHEN
        val result = useCase.invoke()

        // THEN
        assertEquals(SealedResult.Failure(fakeError), result)
    }
}