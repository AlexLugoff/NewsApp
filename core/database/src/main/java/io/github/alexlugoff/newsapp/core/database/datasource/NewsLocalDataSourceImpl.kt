package io.github.alexlugoff.newsapp.core.database.datasource

import io.github.alexlugoff.newsapp.core.database.dao.NewsDao
import io.github.alexlugoff.newsapp.core.database.entities.NewsItemEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsLocalDataSourceImpl @Inject constructor(
    private val newsDao: NewsDao
) : NewsLocalDataSource {

    override fun getAllNewsFlow(): Flow<List<NewsItemEntity>> = newsDao.getAllNewsFlow()

    override suspend fun clearOldNews(timestamp: Long) = newsDao.clearOldNews(timestamp)

    override suspend fun updateCache(news: List<NewsItemEntity>) {
        newsDao.updateCache(news)
    }

    override suspend fun getNewsByLink(link: String): NewsItemEntity? {
        return newsDao.getNewsByLink(link)
    }
}
