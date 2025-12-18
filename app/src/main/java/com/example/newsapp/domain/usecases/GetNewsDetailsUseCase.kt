package com.example.newsapp.domain.usecases

import com.example.newsapp.SealedResult
import com.example.newsapp.data.exception.DataError
import com.example.newsapp.domain.models.NewsItem
import com.example.newsapp.domain.repository.NewsRepository
import javax.inject.Inject

class GetNewsDetailsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(newsLink: String): SealedResult<NewsItem?, DataError> {
        return repository.getNewsDetails(newsLink)
    }
}