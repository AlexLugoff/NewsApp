package com.example.newsapp.core.network

import com.example.newsapp.core.common.result.SealedResult
import com.example.newsapp.core.common.error.DataError
import com.prof18.rssparser.model.RssChannel

interface NewsRemoteDataSource {
    suspend fun getNewsFeed(url: String): SealedResult<RssChannel, DataError.Network>
}
