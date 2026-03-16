package com.example.newsapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.newsapp.data.db.NewsSourceDao
import com.example.newsapp.data.db.entities.NewsSourceEntity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewsSourceDaoTest : BaseDaoTest() {

    private lateinit var dao: NewsSourceDao

    @Before
    override fun createDb() {
        // Вызываем базовую инициализацию базы в памяти
        super.createDb()
        dao = database.newsSourceDao()
    }

    @Test
    fun insertAndGetSourcesFlow_emitsCorrectList() = runTest {
        // Given: Список источников
        val sources = listOf(
            NewsSourceEntity(id = 1, name = "Lenta", url = "url1", isEnabled = true),
            NewsSourceEntity(id = 2, name = "Habr", url = "url2", isEnabled = false)
        )

        // When: Сохраняем в базу
        dao.insertSources(sources)

        // Then: Получаем через Flow и проверяем первый замер
        dao.getSourcesFlow().test {
            val result = awaitItem()
            assertEquals(2, result.size)
            assertEquals("Lenta", result[0].name)
            assertEquals("Habr", result[1].name)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getEnabledSources_returnsOnlyEnabled() = runTest {
        // Given
        val sources = listOf(
            NewsSourceEntity(id = 1, name = "Enabled", url = "url1", isEnabled = true),
            NewsSourceEntity(id = 2, name = "Disabled", url = "url2", isEnabled = false)
        )
        dao.insertSources(sources)

        // When
        val enabledSources = dao.getEnabledSources()

        // Then
        assertEquals(1, enabledSources.size)
        assertEquals("Enabled", enabledSources[0].name)
        assertTrue(enabledSources[0].isEnabled)
    }

    @Test
    fun updateSourceStatus_changesIsEnabledCorrectly() = runTest {
        // Given
        val source = NewsSourceEntity(id = 10, name = "Test", url = "url", isEnabled = false)
        dao.insertSources(listOf(source))

        // When: Переключаем в true
        dao.updateSourceStatus(sourceId = 10, isEnabled = true)

        // Then
        val result = dao.getEnabledSources()
        assertEquals(1, result.size)
        assertEquals(10, result[0].id)
        assertTrue(result[0].isEnabled)
    }

    @Test
    fun insertSources_onConflictReplace_worksCorrectly() = runTest {
        // Given: Источник с ID 1
        val initial = NewsSourceEntity(id = 1, name = "Old Name", url = "url", isEnabled = true)
        dao.insertSources(listOf(initial))

        // When: Вставляем источник с тем же ID, но другим именем
        val updated = NewsSourceEntity(id = 1, name = "New Name", url = "url", isEnabled = true)
        dao.insertSources(listOf(updated))

        // Then
        val result = dao.getEnabledSources()
        assertEquals(1, result.size)
        assertEquals("New Name", result[0].name)
    }
}