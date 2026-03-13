package com.example.newsapp.data.repository

import android.util.Log
import com.example.newsapp.SealedResult
import com.example.newsapp.data.datasource.local.NewsLocalDataSource
import com.example.newsapp.data.datasource.local.NewsSourceLocalDataSource
import com.example.newsapp.data.datasource.remote.NewsRemoteDataSource
import com.example.newsapp.data.db.entities.NewsItemEntity
import com.example.newsapp.data.db.entities.NewsSourceEntity
import com.example.newsapp.data.exception.DataError
import com.example.newsapp.data.mappers.toDomain
import com.example.newsapp.data.mappers.toDomainList
import com.example.newsapp.data.mappers.toEntityList
import com.example.newsapp.data.models.RssFeedDto
import com.example.newsapp.domain.models.NewsItem
import com.example.newsapp.domain.models.NewsSourceItem
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.fold
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class NewsRepositoryImpl @Inject constructor(
    private val remoteDataSource: NewsRemoteDataSource,
    private val newsLocalDataSource: NewsLocalDataSource,
    private val newsSourceLocalDataSource: NewsSourceLocalDataSource,
) : NewsRepository {

    override fun getNewsFlow(): Flow<List<NewsItem>> {
        return newsLocalDataSource.getAllNewsFlow().map { entities ->
            entities.toDomainList()
        }
    }

    override suspend fun refreshNews(): SealedResult<Unit, DataError> {
        val sources = newsSourceLocalDataSource.getEnabledSources()
        if (sources.isEmpty()) return SealedResult.Success(Unit)
        return fetchFromAllSources(sources).fold(
            onSuccess = { newsItemEntityList ->
                newsLocalDataSource.updateCache(newsItemEntityList)
                SealedResult.Success(Unit)
            },
            onFailure = { networkError ->
                SealedResult.Failure(networkError)
            }
        )
    }

    private suspend fun fetchFromAllSources(sources: List<NewsSourceEntity>): SealedResult<List<NewsItemEntity>, DataError> =
        coroutineScope {
            try {
                val allNews = sources
                    .map { source -> async { remoteDataSource.getNewsFeed(source.url) } }
                    .awaitAll()
                    .filterIsInstance<SealedResult.Success<RssFeedDto>>()
                    .flatMap { it.data.toEntityList() }
                    .distinctBy { it.link } // Удаление дубликатов
                    .sortedByDescending { it.pubDate }

                SealedResult.Success(allNews)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Log.e("NewsRepository", "Error fetching news", e)
                SealedResult.Failure(DataError.Network.UNKNOWN)
            }
        }

    override suspend fun getNewsDetails(newsLink: String): SealedResult<NewsItem?, DataError> {
        val entity = newsLocalDataSource.getNewsByLink(newsLink)
        return SealedResult.Success(entity.toDomain())
    }

    override fun getNewsSources(): Flow<List<NewsSourceItem>> =
        newsSourceLocalDataSource.getSourcesFlow().map { list -> list.map { it.toDomain() } }


    override suspend fun toggleSource(sourceId: Int, isEnabled: Boolean) =
        newsSourceLocalDataSource.updateSourceStatus(sourceId, isEnabled)

}