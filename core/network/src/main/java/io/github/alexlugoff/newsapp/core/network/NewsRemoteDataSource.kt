package io.github.alexlugoff.newsapp.core.network

import io.github.alexlugoff.newsapp.core.common.result.SealedResult
import io.github.alexlugoff.newsapp.core.common.error.DataError
import com.prof18.rssparser.model.RssChannel

interface NewsRemoteDataSource {
    suspend fun getNewsFeed(url: String): SealedResult<RssChannel, DataError.Network>
}
