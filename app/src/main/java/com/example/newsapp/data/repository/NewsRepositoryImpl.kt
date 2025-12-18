package com.example.newsapp.data.repository

import com.example.newsapp.SealedResult
import com.example.newsapp.data.datasource.local.NewsLocalDataSource
import com.example.newsapp.data.datasource.remote.NewsRemoteDataSource
import com.example.newsapp.data.exception.DataError
import com.example.newsapp.data.mappers.toDomain
import com.example.newsapp.data.mappers.toDomainList
import com.example.newsapp.data.mappers.toEntityList
import com.example.newsapp.domain.models.NewsItem
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.fold
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val remoteDataSource: NewsRemoteDataSource,
    private val localDataSource: NewsLocalDataSource
) : NewsRepository {

    override suspend fun getNews(): SealedResult<List<NewsItem>, DataError> {
        val remoteResult = remoteDataSource.getNewsFeed()

        return remoteResult.fold(
            onSuccess = { rssFeedDto ->
                val entities = rssFeedDto.toEntityList()
                localDataSource.clearNews()
                localDataSource.saveNews(entities)
                SealedResult.Success(entities.toDomainList())
            },
            onFailure = { networkError ->
                val cachedEntities = localDataSource.getAllNews()
                if (cachedEntities.isNotEmpty()) {
                    SealedResult.Success(cachedEntities.toDomainList())
                } else {
                    SealedResult.Failure(networkError)
                }
            }
        )
    }

    override suspend fun getNewsDetails(newsLink: String): SealedResult<NewsItem?, DataError> {
        val entity = localDataSource.getNewsByLink(newsLink)
        return if (entity != null) {
            SealedResult.Success(entity.toDomain())
        } else {
            SealedResult.Failure(DataError.Local.NOT_FOUND)
        }
    }

}