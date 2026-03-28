package com.example.newsapp.core.domain.repository

import com.example.newsapp.core.common.SealedResult
import com.example.newsapp.core.model.NewsItem
import com.example.newsapp.core.model.NewsSourceItem
import com.example.newsapp.core.common.DataError
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getNewsFlow(): Flow<List<NewsItem>>

    suspend fun clearOldNews()

    suspend fun refreshNews(): SealedResult<Unit, DataError>

    suspend fun getNewsDetails(newsLink: String): SealedResult<NewsItem?, DataError>

    fun getNewsSources(): Flow<List<NewsSourceItem>>

    suspend fun toggleSource(sourceId: Int, isEnabled: Boolean)
}
