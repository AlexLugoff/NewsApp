package com.example.newsapp.data.datasource.local

import com.example.newsapp.data.db.entities.NewsSourceEntity
import kotlinx.coroutines.flow.Flow

interface NewsSourceLocalDataSource {

    fun getSourcesFlow(): Flow<List<NewsSourceEntity>>

    suspend fun getEnabledSources(): List<NewsSourceEntity>

    suspend fun updateSourceStatus(sourceId: Int, isEnabled: Boolean)

    suspend fun insertSources(sources: List<NewsSourceEntity>)
}