package com.example.newsapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.data.db.entities.NewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(news: List<NewsEntity>)

    @Query("SELECT * FROM news ORDER BY pubDate DESC")
    suspend fun getAllNews(): List<NewsEntity>

    @Query("SELECT * FROM news WHERE link = :newsLink")
    suspend fun getNewsByLink(newsLink: String): NewsEntity?

    @Query("DELETE FROM news")
    suspend fun clearNews()
}