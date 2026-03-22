package com.example.newsapp.data

import app.cash.turbine.test
import com.example.newsapp.AppDispatchers
import com.example.newsapp.BaseUnitTest
import com.example.newsapp.MainDispatcherRule
import com.example.newsapp.extensions.SealedResult
import com.example.newsapp.data.datasource.local.NewsLocalDataSource
import com.example.newsapp.data.datasource.local.NewsSourceLocalDataSource
import com.example.newsapp.data.datasource.remote.NewsRemoteDataSource
import com.example.newsapp.data.db.NewsDao
import com.example.newsapp.data.db.NewsSourceDao
import com.example.newsapp.data.db.entities.NewsItemEntity
import com.example.newsapp.data.db.entities.NewsSourceEntity
import com.example.newsapp.data.exception.DataError
import com.example.newsapp.data.mappers.toEntityList
import com.example.newsapp.data.repository.NewsRepositoryImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalCoroutinesApi::class)
class NewsRepositoryImplTest : BaseUnitTest() {

    // Мокаем зависимости
    @MockK
    lateinit var remoteDataSource: NewsRemoteDataSource
    @MockK
    lateinit var newsLocalDataSource: NewsLocalDataSource
    @MockK
    lateinit var newsSourceLocalDataSource: NewsSourceLocalDataSource

    private lateinit var repository: NewsRepositoryImpl

    @Before
    override fun setup() {
        super.setup()
        // testDispatchers берется из BaseUnitTest (содержит тестовые диспетчеры)
        repository = NewsRepositoryImpl(
            remoteDataSource = remoteDataSource,
            newsLocalDataSource = newsLocalDataSource,
            newsSourceLocalDataSource = newsSourceLocalDataSource,
            dispatchers = testDispatchers
        )
    }

    @Test
    fun `refreshNews - when no enabled sources - returns success early`() = runTest {
        // Given: База возвращает пустой список источников
        coEvery { newsSourceLocalDataSource.getEnabledSources() } returns emptyList()

        // When
        val result = repository.refreshNews()

        // Then
        assertTrue(result is SealedResult.Success)
        // Проверяем, что запрос в сеть даже не пытался уйти
        coVerify(exactly = 0) { remoteDataSource.getNewsFeed(any()) }
    }

    @Test
    fun `refreshNews - when multiple sources - fetches in parallel and updates cache`() = runTest {
        // Given: Два активных источника
        val sources = listOf(
            NewsSourceEntity(1, "Source 1", "url1", true),
            NewsSourceEntity(2, "Source 2", "url2", true)
        )
        val mockDto = mockk<RssFeedDto> {
            every { toEntityList() } returns listOf(
                NewsItemEntity("link1", "Title", "Desc", null, 1000L, "Category")
            )
        }

        coEvery { newsSourceLocalDataSource.getEnabledSources() } returns sources
        coEvery { remoteDataSource.getNewsFeed(any()) } returns SealedResult.Success(mockDto)
        coEvery { newsLocalDataSource.updateCache(any()) } returns Unit

        // When
        val result = repository.refreshNews()

        // Then
        assertTrue(result is SealedResult.Success)
        // Проверяем, что сеть была вызвана для КАЖДОГО URL
        coVerify(exactly = 1) { remoteDataSource.getNewsFeed("url1") }
        coVerify(exactly = 1) { remoteDataSource.getNewsFeed("url2") }
        // Проверяем, что кэш обновился итоговым списком
        coVerify { newsLocalDataSource.updateCache(any()) }
    }

    @Test
    fun `refreshNews - when all sources fail - returns failure`() = runTest {
        // Given
        val sources = listOf(NewsSourceEntity(1, "S1", "url", true))
        coEvery { newsSourceLocalDataSource.getEnabledSources() } returns sources
        coEvery { remoteDataSource.getNewsFeed(any()) } returns SealedResult.Failure(DataError.Network.Unknown())

        // When
        val result = repository.refreshNews()

        // Then
        assertTrue(result is SealedResult.Failure)
        assertEquals(DataError.Network.Unknown(), (result as SealedResult.Failure).error)
    }

    @Test
    fun `clearOldNews - calculates threshold and calls local data source`() = runTest {
        // Given
        coEvery { newsLocalDataSource.clearOldNews(any()) } returns Unit

        // When
        repository.clearOldNews()

        // Then
        // Проверяем, что метод был вызван. Точное время проверить сложно из-за Clock.System.now(),
        // но можно проверить сам факт взаимодействия.
        coVerify { newsLocalDataSource.clearOldNews(any()) }
    }

    @Test
    fun `getNewsDetails - returns mapped domain model`() = runTest {
        // Given
        val link = "test_link"
        val mockEntity = NewsItemEntity(link, "Title", "Desc", null, 123L, "Category")
        coEvery { newsLocalDataSource.getNewsByLink(link) } returns mockEntity

        // When
        val result = repository.getNewsDetails(link)

        // Then
        assertTrue(result is SealedResult.Success)
        val data = (result as SealedResult.Success).data
        assertNotNull(data)
        assertEquals("Title", data?.title)
    }

    @Test
    fun `toggleSource - updates status in local source`() = runTest {
        // Given
        coEvery { newsSourceLocalDataSource.updateSourceStatus(any(), any()) } returns Unit

        // When
        repository.toggleSource(1, true)

        // Then
        coVerify { newsSourceLocalDataSource.updateSourceStatus(1, true) }
    }
}