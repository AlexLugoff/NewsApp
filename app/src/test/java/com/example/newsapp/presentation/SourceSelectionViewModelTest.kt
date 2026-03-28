package com.example.newsapp.presentation

import app.cash.turbine.test
import com.example.newsapp.BaseUnitTest
import com.example.newsapp.core.domain.usecase.GetNewsSourcesFlowUseCase
import com.example.newsapp.core.domain.usecase.ToggleSourceUseCase
import com.example.newsapp.core.model.NewsSourceItem
import com.example.newsapp.core.ui.util.UiText
import com.example.newsapp.presentation.source_selection.SourceSelectionViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SourceSelectionViewModelTest : BaseUnitTest() {

    @MockK
    lateinit var getNewsSourcesUseCase: GetNewsSourcesFlowUseCase

    @MockK
    lateinit var toggleSourceUseCase: ToggleSourceUseCase

    private lateinit var viewModel: SourceSelectionViewModel

    private val sourcesFlow = MutableStateFlow<List<NewsSourceItem>>(emptyList())

    @Before
    override fun setup() {
        super.setup()
        every { getNewsSourcesUseCase() } returns sourcesFlow
        viewModel = SourceSelectionViewModel(getNewsSourcesUseCase, toggleSourceUseCase)
    }

    @Test
    fun `sourcesState - reflects data from use case flow`() = runTest {
        val mockSources = listOf(
            NewsSourceItem(1, "Habr", "link", true),
            NewsSourceItem(2, "Lenta", "link", false)
        )

        viewModel.sourcesState.test {
            // Начальное значение StateFlow
            assertEquals(emptyList<NewsSourceItem>(), awaitItem())

            // Эмулируем обновление данных в БД
            sourcesFlow.value = mockSources
            assertEquals(mockSources, awaitItem())
        }
    }

    @Test
    fun `toggleSource - calls use case with inverted isEnabled value`() = runTest {
        // Given
        val source = NewsSourceItem(1, "Habr", "link", isEnabled = true)
        coEvery { toggleSourceUseCase(any(), any()) } returns Unit

        // When
        viewModel.toggleSource(source)

        // Then
        coVerify { toggleSourceUseCase(1, false) }
    }

    @Test
    fun `toggleSource - when use case throws - emits error event`() = runTest {
        // Given
        val source = NewsSourceItem(1, "Habr", "link", isEnabled = true)
        val exceptionMessage = "Test Error"
        coEvery { toggleSourceUseCase(any(), any()) } throws Exception(exceptionMessage)

        // When & Then
        viewModel.errorEvent.test {
            viewModel.toggleSource(source)

            val error = awaitItem()
            assertTrue(error is UiText.StringResource)
            assertEquals(
                com.example.newsapp.R.string.error_source_parser,
                (error as UiText.StringResource).id
            )
            // Проверяем, что сообщение об ошибке передано в аргументы UiText
            assertEquals(exceptionMessage, error.args[0])
        }
    }
}
