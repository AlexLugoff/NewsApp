package com.example.newsapp.core.database.datasource

import com.example.newsapp.core.database.entities.NewsSourceEntity
import kotlinx.coroutines.flow.Flow

interface NewsSourceLocalDataSource {
    fun getSourcesFlow(): Flow<List<NewsSourceEntity>>
    suspend fun getEnabledSources(): List<NewsSourceEntity>
    suspend fun updateSourceStatus(sourceId: Int, isEnabled: Boolean)
    suspend fun insertSources(sources: List<NewsSourceEntity>)
}
