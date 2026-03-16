package com.example.newsapp.usecase

import com.example.newsapp.BaseUnitTest
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.domain.usecases.RefreshNewsUseCase
import com.example.newsapp.extensions.SealedResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RefreshNewsUseCaseTest : BaseUnitTest() {

    @MockK
    lateinit var repository: NewsRepository

    private lateinit var useCase: RefreshNewsUseCase

    @Before
    override fun setup() {
        super.setup()
        useCase = RefreshNewsUseCase(repository)
    }

    @Test
    fun `invoke - returns result from repository`() = runTest {
        // Given
        val expectedResult = SealedResult.Success(Unit)
        coEvery { repository.refreshNews() } returns expectedResult

        // When
        val result = useCase.invoke()

        // Then
        assertEquals(expectedResult, result)
        coVerify(exactly = 1) { repository.refreshNews() }
    }
}