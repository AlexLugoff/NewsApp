package com.example.newsapp.core.database.datasource

import com.example.newsapp.core.database.dao.NewsSourceDao
import com.example.newsapp.core.database.entities.NewsSourceEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsSourceLocalDataSourceImpl @Inject constructor(
    private val newsSourceDao: NewsSourceDao
) : NewsSourceLocalDataSource {

    override fun getSourcesFlow(): Flow<List<NewsSourceEntity>> =
        newsSourceDao.getSourcesFlow()


    override suspend fun getEnabledSources(): List<NewsSourceEntity> =
        newsSourceDao.getEnabledSources()


    override suspend fun updateSourceStatus(sourceId: Int, isEnabled: Boolean) =
        newsSourceDao.updateSourceStatus(sourceId, isEnabled)


    override suspend fun insertSources(sources: List<NewsSourceEntity>) =
        newsSourceDao.insertSources(sources)
}
