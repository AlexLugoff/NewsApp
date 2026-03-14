package com.example.newsapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.newsapp.data.db.entities.NewsItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: List<NewsItemEntity>)

    @Query("DELETE FROM news")
    suspend fun clearAllNews()

    @Query("DELETE FROM news WHERE pubDate < :timestamp")
    suspend fun clearOldNews(timestamp: Long)

    @Query("SELECT * FROM news ORDER BY pubDate DESC")
    fun getAllNewsFlow(): Flow<List<NewsItemEntity>>

    @Transaction
    suspend fun updateCache(news: List<NewsItemEntity>) {
        clearAllNews()
        insertNews(news)
    }

    @Query("SELECT * FROM news WHERE link = :newsLink")
    suspend fun getNewsByLink(newsLink: String): NewsItemEntity
}