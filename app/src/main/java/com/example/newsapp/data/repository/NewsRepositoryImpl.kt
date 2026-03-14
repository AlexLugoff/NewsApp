package com.example.newsapp.data.repository

import com.example.newsapp.AppDispatchers
import com.example.newsapp.NEWS_EXPIRATION_THRESHOLD
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
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class NewsRepositoryImpl @Inject constructor(
    private val remoteDataSource: NewsRemoteDataSource,
    private val newsLocalDataSource: NewsLocalDataSource,
    private val newsSourceLocalDataSource: NewsSourceLocalDataSource,
    private val dispatchers: AppDispatchers
) : NewsRepository {

    override fun getNewsFlow(): Flow<List<NewsItem>> {
        return newsLocalDataSource.getAllNewsFlow().map { entities ->
            entities.toDomainList()
        }
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun clearOldNews() = withContext(dispatchers.io) {
        val threshold = Clock.System.now()
            .minus(NEWS_EXPIRATION_THRESHOLD)
            .toEpochMilliseconds()

        try {
            newsLocalDataSource.clearOldNews(threshold)
            Timber.d("Old news cleared successfully. Threshold: $threshold")
        } catch (e: Exception) {
            Timber.e(e, "Failed to clear old news")
        }
    }

    override suspend fun refreshNews(): SealedResult<Unit, DataError> =
        withContext(dispatchers.io) {
            val sources = newsSourceLocalDataSource.getEnabledSources()
            if (sources.isEmpty()) return@withContext SealedResult.Success(Unit)
            fetchFromAllSources(sources).fold(
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
                val deferredResults = sources.map { source ->
                    async { remoteDataSource.getNewsFeed(source.url) }
                }
                val results = deferredResults.awaitAll()
                // Проверяем, есть ли хоть один успех
                val successResults = results.filterIsInstance<SealedResult.Success<RssFeedDto>>()

                if (successResults.isEmpty() && results.isNotEmpty()) {
                    // Если источников много, а успехов 0 — значит, это общая ошибка сети
                    val firstError = results.filterIsInstance<SealedResult.Failure<DataError>>()
                        .firstOrNull()?.error ?: DataError.Network.UNKNOWN
                    return@coroutineScope SealedResult.Failure(firstError)
                }

                val allNews = successResults
                    .flatMap { it.data.toEntityList() }
                    .distinctBy { it.link }
                    .sortedByDescending { it.pubDate }

                SealedResult.Success(allNews)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Timber.e(e, "Критическая ошибка при загрузке новостей")
                SealedResult.Failure(DataError.Network.UNKNOWN)
            }
        }

    override suspend fun getNewsDetails(newsLink: String): SealedResult<NewsItem?, DataError> =
        withContext(dispatchers.io) {
            val entity = newsLocalDataSource.getNewsByLink(newsLink)
            SealedResult.Success(entity.toDomain())
        }

    override fun getNewsSources(): Flow<List<NewsSourceItem>> =
        newsSourceLocalDataSource.getSourcesFlow().map { list -> list.map { it.toDomain() } }


    override suspend fun toggleSource(sourceId: Int, isEnabled: Boolean) =
        withContext(dispatchers.io) {
            newsSourceLocalDataSource.updateSourceStatus(sourceId, isEnabled)
        }

}