package com.example.newsapp.data.datasource.local

import com.example.newsapp.data.db.NewsDao
import com.example.newsapp.data.db.entities.NewsItemEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsLocalDataSourceImpl @Inject constructor(
    private val newsDao: NewsDao
) : NewsLocalDataSource {

    override fun getAllNewsFlow(): Flow<List<NewsItemEntity>> = newsDao.getAllNewsFlow()

    override suspend fun updateCache(news: List<NewsItemEntity>) {
        newsDao.updateCache(news)
    }

    override suspend fun getNewsByLink(link: String): NewsItemEntity {
        return newsDao.getNewsByLink(link)
    }
}