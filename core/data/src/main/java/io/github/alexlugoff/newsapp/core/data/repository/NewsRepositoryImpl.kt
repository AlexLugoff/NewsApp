package io.github.alexlugoff.newsapp.core.data.repository

import io.github.alexlugoff.newsapp.core.common.dispatchers.AppDispatchers
import io.github.alexlugoff.newsapp.core.common.error.DataError
import io.github.alexlugoff.newsapp.core.common.util.NEWS_EXPIRATION_THRESHOLD
import io.github.alexlugoff.newsapp.core.common.result.SealedResult
import io.github.alexlugoff.newsapp.core.common.result.fold
import io.github.alexlugoff.newsapp.core.data.mappers.toDomain
import io.github.alexlugoff.newsapp.core.data.mappers.toDomainList
import io.github.alexlugoff.newsapp.core.data.mappers.toEntityList
import io.github.alexlugoff.newsapp.core.database.datasource.NewsLocalDataSource
import io.github.alexlugoff.newsapp.core.database.datasource.NewsSourceLocalDataSource
import io.github.alexlugoff.newsapp.core.database.entities.NewsItemEntity
import io.github.alexlugoff.newsapp.core.database.entities.NewsSourceEntity
import io.github.alexlugoff.newsapp.core.domain.repository.NewsRepository
import io.github.alexlugoff.newsapp.core.model.NewsItem
import io.github.alexlugoff.newsapp.core.model.NewsSourceItem
import io.github.alexlugoff.newsapp.core.network.NewsRemoteDataSource
import com.prof18.rssparser.model.RssChannel
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
                val successResults = results.filterIsInstance<SealedResult.Success<RssChannel>>()

                val allNews = successResults
                    .flatMap { it.data.toEntityList() }
                    .distinctBy { it.link }
                    .sortedByDescending { it.pubDate }

                SealedResult.Success(allNews)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Timber.e(e, e.localizedMessage)
                SealedResult.Failure(DataError.Network.Unknown(e.localizedMessage))
            }
        }

    override suspend fun getNewsDetails(newsLink: String): SealedResult<NewsItem?, DataError> =
        withContext(dispatchers.io) {
            val entity = newsLocalDataSource.getNewsByLink(newsLink)
            SealedResult.Success(entity?.toDomain())
        }

    override fun getNewsSources(): Flow<List<NewsSourceItem>> =
        newsSourceLocalDataSource.getSourcesFlow().map { list -> list.map { it.toDomain() } }


    override suspend fun toggleSource(sourceId: Int, isEnabled: Boolean) =
        withContext(dispatchers.io) {
            newsSourceLocalDataSource.updateSourceStatus(sourceId, isEnabled)
        }

}
