package com.example.newsapp.data

import com.example.newsapp.SealedResult
import com.example.newsapp.data.datasource.local.NewsLocalDataSource
import com.example.newsapp.data.datasource.remote.NewsRemoteDataSource
import com.example.newsapp.data.db.entities.NewsEntity
import com.example.newsapp.data.exception.DataError
import com.example.newsapp.data.models.RssChannelDto
import com.example.newsapp.data.models.RssFeedDto
import com.example.newsapp.data.repository.NewsRepositoryImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class NewsRepositoryImplTest {

    private val mockRemoteDataSource = mockk<NewsRemoteDataSource>()
    private val mockLocalDataSource = mockk<NewsLocalDataSource>(relaxed = true)
    private lateinit var repository: NewsRepositoryImpl

    private val fakeRss = RssFeedDto(RssChannelDto(emptyList())) // Пустой, но успешный DTO
    private val fakeEntities =
        listOf(NewsEntity("cache_link", "Cache Title", "Cache Desc", null, 1700000000000L))

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = NewsRepositoryImpl(mockRemoteDataSource, mockLocalDataSource)
    }

    @Test
    fun `getNews returns data from network and caches when remote is Success`() = runTest {
        // GIVEN
        coEvery { mockRemoteDataSource.getNewsFeed() } returns SealedResult.Success(fakeRss)

        // WHEN
        val result = repository.getNews()

        // THEN
        assertTrue(result is SealedResult.Success)
        coVerify(exactly = 1) { mockLocalDataSource.saveNews(any()) } // Проверяем кэширование
    }

    @Test
    fun `getNews returns cache when remote is Failure but local data exists`() = runTest {
        // GIVEN: Сеть падает, но кэш доступен
        coEvery { mockRemoteDataSource.getNewsFeed() } returns SealedResult.Failure(DataError.Network.UNKNOWN_HOST)
        coEvery { mockLocalDataSource.getAllNews() } returns fakeEntities

        // WHEN
        val result = repository.getNews()

        // THEN
        assertTrue(result is SealedResult.Success)
        val newsList = (result as SealedResult.Success).data
        assertEquals("Cache Title", newsList.first().title)
        coVerify(exactly = 1) { mockLocalDataSource.getAllNews() }
    }

    @Test
    fun `getNews returns network error when remote fails and local is empty`() = runTest {
        // GIVEN: Сеть падает, и кэш пуст
        val networkError = DataError.Network.CONNECTION_TIMEOUT
        coEvery { mockRemoteDataSource.getNewsFeed() } returns SealedResult.Failure(networkError)
        coEvery { mockLocalDataSource.getAllNews() } returns emptyList()

        // WHEN
        val result = repository.getNews()

        // THEN
        assertTrue(result is SealedResult.Failure)
        assertEquals(networkError, (result as SealedResult.Failure).error)
    }
}