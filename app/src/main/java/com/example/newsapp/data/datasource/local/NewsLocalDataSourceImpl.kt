package com.example.newsapp.data.datasource.local

import com.example.newsapp.data.db.NewsDao
import com.example.newsapp.data.db.entities.NewsEntity
import javax.inject.Inject

class NewsLocalDataSourceImpl @Inject constructor(
    private val newsDao: NewsDao
) : NewsLocalDataSource {

    override suspend fun getAllNews(): List<NewsEntity> {
        return newsDao.getAllNews()
    }

    override suspend fun getNewsByLink(link: String): NewsEntity? {
        return newsDao.getNewsByLink(link)
    }

    override suspend fun saveNews(news: List<NewsEntity>) {
        newsDao.insertAll(news)
    }

    override suspend fun clearNews() {
        newsDao.clearNews()
    }
}