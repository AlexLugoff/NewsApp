package com.example.newsapp.data.datasource.remote

import com.example.newsapp.SealedResult
import com.example.newsapp.data.exception.DataError
import com.example.newsapp.data.models.RssFeedDto

interface NewsRemoteDataSource {
    suspend fun getNewsFeed(): SealedResult<RssFeedDto, DataError.Network>
}