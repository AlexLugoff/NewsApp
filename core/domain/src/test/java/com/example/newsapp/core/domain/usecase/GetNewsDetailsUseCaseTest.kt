package com.example.newsapp.core.domain.usecase

import com.example.newsapp.core.domain.BaseUnitTest
import com.example.newsapp.core.domain.repository.NewsRepository
import com.example.newsapp.core.model.NewsItem
import com.example.newsapp.core.common.result.SealedResult
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetNewsDetailsUseCaseTest : BaseUnitTest() {

    @MockK
    lateinit var repository: NewsRepository

    private lateinit var useCase: GetNewsDetailsUseCase

    @Before
    override fun setup() {
        super.setup()
        useCase = GetNewsDetailsUseCase(repository)
    }

    @Test
    fun `invoke - with valid link - returns news item`() = runTest {
        // Given
        val link = "https://example.com/news1"
        val mockNews = mockk<NewsItem>()
        coEvery { repository.getNewsDetails(link) } returns SealedResult.Success(mockNews)

        // When
        val result = useCase.invoke(link)

        // Then
        assertTrue(result is SealedResult.Success)
        assertEquals(mockNews, (result as SealedResult.Success).data)
    }
}
