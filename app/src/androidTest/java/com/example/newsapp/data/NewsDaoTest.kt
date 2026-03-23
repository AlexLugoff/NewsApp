package com.example.newsapp.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.newsapp.data.db.NewsDao
import com.example.newsapp.data.db.entities.NewsItemEntity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewsDaoTest : BaseDaoTest() {

    private lateinit var dao: NewsDao

    @Before
    override fun createDb() {
        super.createDb()
        dao = database.newsDao()
    }

    @Test
    fun insertAndGetAllNewsFlow_emitsSortedByDate() = runTest {
        // Given: Две новости с разным временем публикации
        val newsOld = NewsItemEntity(
            link = "link_old",
            title = "Old News",
            description = "D",
            imageUrl = null,
            pubDate = 1000L
        )
        val newsNew = NewsItemEntity(
            link = "link_new",
            title = "New News",
            description = "D",
            imageUrl = null,
            pubDate = 5000L
        )

        // When
        dao.upsertNews(listOf(newsOld, newsNew))

        // Then: Проверяем, что Flow выдает список, где свежая новость (5000L) первая
        dao.getAllNewsFlow().test {
            val list = awaitItem()
            assertEquals(2, list.size)
            assertEquals("link_new", list[0].link)
            assertEquals("link_old", list[1].link)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateCache_clearsOldAndInsertsNew() = runTest {
        // Given: В базе уже есть старые данные
        val oldNews = NewsItemEntity("old_link", "Old", "D", null, 1000L)
        dao.upsertNews(listOf(oldNews))

        // When: Вызываем транзакцию обновления кэша с новыми данными
        val newNews = listOf(
            NewsItemEntity("new_link_1", "New 1", "D", null, 2000L),
            NewsItemEntity("new_link_2", "New 2", "D", null, 3000L)
        )
        dao.updateCache(newNews)

        // Then: База должна содержать ТОЛЬКО новые данные
        val result = dao.getAllNewsFlow().first()
        assertEquals(2, result.size)
        assertFalse(result.any { it.link == "old_link" })
        assertTrue(result.any { it.link == "new_link_1" })
    }

    @Test
    fun clearOldNews_deletesOnlyItemsBeforeThreshold() = runTest {
        // Given: Три новости: очень старая, на границе и свежая
        val threshold = 2000L
        val newsList = listOf(
            NewsItemEntity("very_old", "T", "D", null, 500L),  // Должна удалиться
            NewsItemEntity("border", "T", "D", null, 1999L),   // Должна удалиться
            NewsItemEntity("fresh", "T", "D", null, 2500L)     // Должна остаться
        )
        dao.upsertNews(newsList)

        // When
        dao.clearOldNews(threshold)

        // Then
        val result = dao.getAllNewsFlow().first()
        assertEquals(1, result.size)
        assertEquals("fresh", result[0].link)
    }

    @Test
    fun getNewsByLink_returnsCorrectEntity() = runTest {
        // Given
        val link = "https://target-link.com"
        val news = NewsItemEntity(link, "Target Title", "D", null, 12345L)
        dao.upsertNews(listOf(news, NewsItemEntity("other", "T", "D", null, 0L)))

        // When
        val result = dao.getNewsByLink(link)

        // Then
        assertNotNull(result)
        assertEquals("Target Title", result?.title)
    }
}