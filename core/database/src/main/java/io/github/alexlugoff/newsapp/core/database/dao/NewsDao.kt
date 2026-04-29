package io.github.alexlugoff.newsapp.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import io.github.alexlugoff.newsapp.core.database.entities.NewsItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Upsert
    suspend fun upsertNews(news: List<NewsItemEntity>)

    @Query("DELETE FROM news")
    suspend fun clearAllNews()

    @Query("DELETE FROM news WHERE pubDate < :timestamp")
    suspend fun clearOldNews(timestamp: Long)

    @Query("SELECT * FROM news ORDER BY pubDate DESC")
    fun getAllNewsFlow(): Flow<List<NewsItemEntity>>

    @Transaction
    suspend fun updateCache(news: List<NewsItemEntity>) {
        clearAllNews()
        upsertNews(news)
    }

    @Query("SELECT * FROM news WHERE link = :newsLink")
    suspend fun getNewsByLink(newsLink: String): NewsItemEntity?
}
