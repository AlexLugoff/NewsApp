package io.github.alexlugoff.newsapp.core.domain.repository

import io.github.alexlugoff.newsapp.core.common.result.SealedResult
import io.github.alexlugoff.newsapp.core.model.NewsItem
import io.github.alexlugoff.newsapp.core.model.NewsSourceItem
import io.github.alexlugoff.newsapp.core.common.error.DataError
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getNewsFlow(): Flow<List<NewsItem>>

    suspend fun clearOldNews()

    suspend fun refreshNews(): SealedResult<Unit, DataError>

    suspend fun getNewsDetails(newsLink: String): SealedResult<NewsItem?, DataError>

    fun getNewsSources(): Flow<List<NewsSourceItem>>

    suspend fun toggleSource(sourceId: Int, isEnabled: Boolean)
}
