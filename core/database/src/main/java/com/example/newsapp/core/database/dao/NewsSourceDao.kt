package com.example.newsapp.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.core.database.entities.NewsSourceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsSourceDao {
    @Query("SELECT * FROM news_sources")
    fun getSourcesFlow(): Flow<List<NewsSourceEntity>>

    @Query("SELECT * FROM news_sources WHERE isEnabled = 1")
    suspend fun getEnabledSources(): List<NewsSourceEntity>

    @Query("UPDATE news_sources SET isEnabled = :isEnabled WHERE id = :sourceId")
    suspend fun updateSourceStatus(sourceId: Int, isEnabled: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSources(sources: List<NewsSourceEntity>)
}
