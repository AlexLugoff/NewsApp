package com.example.newsapp.core.domain.usecase

import com.example.newsapp.core.domain.BaseUnitTest
import com.example.newsapp.core.domain.repository.NewsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ClearOldNewsUseCaseTest : BaseUnitTest() {

    @MockK
    lateinit var repository: NewsRepository

    private lateinit var useCase: ClearOldNewsUseCase

    @Before
    override fun setup() {
        super.setup()
        useCase = ClearOldNewsUseCase(repository)
    }

    @Test
    fun `invoke - calls repository to clear old news`() = runTest {
        // Given
        coEvery { repository.clearOldNews() } returns Unit

        // When
        useCase.invoke()

        // Then
        coVerify(exactly = 1) { repository.clearOldNews() }
    }
}
