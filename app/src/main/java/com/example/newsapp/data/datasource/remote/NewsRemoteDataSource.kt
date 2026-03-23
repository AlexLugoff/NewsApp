package com.example.newsapp.data.datasource.remote

import com.example.newsapp.data.exception.DataError
import com.example.newsapp.extensions.SealedResult
import com.prof18.rssparser.model.RssChannel

interface NewsRemoteDataSource {
    suspend fun getNewsFeed(url: String): SealedResult<RssChannel, DataError.Network>
}