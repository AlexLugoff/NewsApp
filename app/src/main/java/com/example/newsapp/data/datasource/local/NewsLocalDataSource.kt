package com.example.newsapp.data.datasource.local

import com.example.newsapp.data.db.entities.NewsEntity

interface NewsLocalDataSource {
    suspend fun getAllNews(): List<NewsEntity>
    suspend fun getNewsByLink(link: String): NewsEntity?
    suspend fun saveNews(news: List<NewsEntity>)
    suspend fun clearNews()
}