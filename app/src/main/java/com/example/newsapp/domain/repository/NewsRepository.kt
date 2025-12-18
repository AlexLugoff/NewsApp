package com.example.newsapp.domain.repository

import com.example.newsapp.SealedResult
import com.example.newsapp.data.exception.DataError
import com.example.newsapp.domain.models.NewsItem

interface NewsRepository {
    suspend fun getNews(): SealedResult<List<NewsItem>, DataError>

    suspend fun getNewsDetails(newsLink: String): SealedResult<NewsItem?, DataError>
}