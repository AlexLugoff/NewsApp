package io.github.alexlugoff.newsapp.core.database.datasource

import io.github.alexlugoff.newsapp.core.database.entities.NewsItemEntity
import kotlinx.coroutines.flow.Flow

interface NewsLocalDataSource {
    fun getAllNewsFlow(): Flow<List<NewsItemEntity>>
    suspend fun clearOldNews(timestamp: Long)
    suspend fun updateCache(news: List<NewsItemEntity>)
    suspend fun getNewsByLink(link: String): NewsItemEntity?
}
