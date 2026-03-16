package com.example.newsapp.presentation

import app.cash.turbine.test
import com.example.newsapp.BaseUnitTest
import com.example.newsapp.domain.models.NewsSourceItem
import com.example.newsapp.domain.usecases.GetNewsSourcesFlowUseCase
import com.example.newsapp.domain.usecases.ToggleSourceUseCase
import com.example.newsapp.presentation.source_selection.SourceSelectionViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SourceSelectionViewModelTest : BaseUnitTest() {

    @MockK
    lateinit var getNewsSourcesUseCase: GetNewsSourcesFlowUseCase

    @MockK
    lateinit var toggleSourceUseCase: ToggleSourceUseCase

    private lateinit var viewModel: SourceSelectionViewModel

    @Before
    override fun setup() {
        super.setup()
    }

    @Test
    fun `sourcesState - initially emits empty list and then updates from use case`() = runTest {
        // Given
        val mockSources = listOf(
            NewsSourceItem(1, "Habr", "link", true),
            NewsSourceItem(2, "Lenta", "link", false)
        )
        // Имитируем поток данных из репозитория/базы
        every { getNewsSourcesUseCase() } returns flowOf(mockSources)

        // When
        viewModel = SourceSelectionViewModel(getNewsSourcesUseCase, toggleSourceUseCase)

        // Then
        viewModel.sourcesState.test {
            // StateFlow всегда сначала отдает initialValue (в коде это emptyList())
            assertEquals(emptyList<NewsSourceItem>(), awaitItem())

            // Затем получаем данные из UseCase
            assertEquals(mockSources, awaitItem())
        }
    }

    @Test
    fun `toggleSource - when success - calls toggleSourceUseCase with inverted status`() = runTest {
        // Given
        every { getNewsSourcesUseCase() } returns flowOf(emptyList())
        coEvery { toggleSourceUseCase(any(), any()) } returns Unit

        viewModel = SourceSelectionViewModel(getNewsSourcesUseCase, toggleSourceUseCase)
        val source = NewsSourceItem(10, "Test", "link", isEnabled = true)

        // When
        viewModel.toggleSource(source)

        // Then
        // Проверяем, что в UseCase ушел id=10 и статус false (инверсия true)
        coVerify { toggleSourceUseCase(10, false) }
    }

    @Test
    fun `toggleSource - when failure - emits error message to errorEvent`() = runTest {
        // Given
        every { getNewsSourcesUseCase() } returns flowOf(emptyList())
        val errorMessage = "Database Error"
        coEvery { toggleSourceUseCase(any(), any()) } throws Exception(errorMessage)

        viewModel = SourceSelectionViewModel(getNewsSourcesUseCase, toggleSourceUseCase)
        val source = NewsSourceItem(1, "Test", "link", isEnabled = true)

        // When & Then
        viewModel.errorEvent.test {
            viewModel.toggleSource(source)

            val receivedError = awaitItem()
            assertTrue(receivedError.contains(errorMessage))
        }
    }
}