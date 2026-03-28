package com.example.newsapp.core.data.repository

import com.example.newsapp.core.data.BaseUnitTest
import com.example.newsapp.core.common.error.DataError
import com.example.newsapp.core.common.result.SealedResult
import com.example.newsapp.core.database.datasource.NewsLocalDataSource
import com.example.newsapp.core.database.datasource.NewsSourceLocalDataSource
import com.example.newsapp.core.database.entities.NewsItemEntity
import com.example.newsapp.core.database.entities.NewsSourceEntity
import com.example.newsapp.core.network.NewsRemoteDataSource
import com.prof18.rssparser.model.RssChannel
import com.prof18.rssparser.model.RssItem
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NewsRepositoryImplTest : BaseUnitTest() {

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
        repository = NewsRepositoryImpl(
            remoteDataSource = remoteDataSource,
            newsLocalDataSource = newsLocalDataSource,
            newsSourceLocalDataSource = newsSourceLocalDataSource,
            dispatchers = testDispatchers
        )
    }

    @Test
    fun `refreshNews - when no enabled sources - returns success early`() = runTest {
        coEvery { newsSourceLocalDataSource.getEnabledSources() } returns emptyList()

        val result = repository.refreshNews()

        assertTrue(result is SealedResult.Success)
        coVerify(exactly = 0) { remoteDataSource.getNewsFeed(any()) }
    }

    @Test
    fun `refreshNews - when multiple sources - fetches and updates cache`() = runTest {
        val sources = listOf(
            NewsSourceEntity(1, "Source 1", "url1", true),
            NewsSourceEntity(2, "Source 2", "url2", true)
        )

        val mockRssItem = mockk<RssItem>(relaxed = true) {
            every { title } returns "Title"
            every { link } returns "link1"
            every { pubDate } returns "Date"
        }

        val mockChannel = mockk<RssChannel>(relaxed = true) {
            every { items } returns listOf(mockRssItem)
        }

        coEvery { newsSourceLocalDataSource.getEnabledSources() } returns sources
        coEvery { remoteDataSource.getNewsFeed(any()) } returns SealedResult.Success(mockChannel)
        coEvery { newsLocalDataSource.updateCache(any()) } returns Unit

        val result = repository.refreshNews()

        assertTrue(result is SealedResult.Success)
        coVerify(exactly = 1) { remoteDataSource.getNewsFeed("url1") }
        coVerify(exactly = 1) { remoteDataSource.getNewsFeed("url2") }
        coVerify { newsLocalDataSource.updateCache(any()) }
    }

    @Test
    fun `refreshNews - when exception occurs during fetch - returns failure`() = runTest {
        val sources = listOf(NewsSourceEntity(1, "S1", "url", true))
        coEvery { newsSourceLocalDataSource.getEnabledSources() } returns sources
        coEvery { remoteDataSource.getNewsFeed(any()) } throws Exception("Network error")

        val result = repository.refreshNews()

        assertTrue(result is SealedResult.Failure)
        assertTrue((result as SealedResult.Failure).error is DataError.Network.Unknown)
    }

    @Test
    fun `getNewsDetails - returns mapped domain model`() = runTest {
        val link = "test_link"
        val mockEntity = NewsItemEntity(link, "Title", "Desc", null, 123L)
        coEvery { newsLocalDataSource.getNewsByLink(link) } returns mockEntity

        val result = repository.getNewsDetails(link)

        assertTrue(result is SealedResult.Success)
        val data = (result as SealedResult.Success).data
        assertNotNull(data)
        assertEquals("Title", data?.title)
    }

    @Test
    fun `toggleSource - calls local source status update`() = runTest {
        coEvery { newsSourceLocalDataSource.updateSourceStatus(any(), any()) } returns Unit

        repository.toggleSource(1, true)

        coVerify { newsSourceLocalDataSource.updateSourceStatus(1, true) }
    }

    @Test
    fun `clearOldNews - calls local source to clear old data`() = runTest {
        coEvery { newsLocalDataSource.clearOldNews(any()) } returns Unit

        repository.clearOldNews()

        coVerify { newsLocalDataSource.clearOldNews(any()) }
    }
}
