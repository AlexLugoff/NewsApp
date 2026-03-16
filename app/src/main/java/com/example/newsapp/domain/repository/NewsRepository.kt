package com.example.newsapp.domain.repository

import com.example.newsapp.data.exception.DataError
import com.example.newsapp.domain.models.NewsItem
import com.example.newsapp.domain.models.NewsSourceItem
import com.example.newsapp.extensions.SealedResult
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getNewsFlow(): Flow<List<NewsItem>>

    suspend fun clearOldNews()

    suspend fun refreshNews(): SealedResult<Unit, DataError>

    suspend fun getNewsDetails(newsLink: String): SealedResult<NewsItem?, DataError>

    fun getNewsSources(): Flow<List<NewsSourceItem>>

    suspend fun toggleSource(sourceId: Int, isEnabled: Boolean)
}