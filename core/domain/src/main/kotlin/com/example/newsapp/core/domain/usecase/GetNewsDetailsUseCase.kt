package com.example.newsapp.core.domain.usecase

import com.example.newsapp.core.common.error.DataError
import com.example.newsapp.core.common.result.SealedResult
import com.example.newsapp.core.domain.repository.NewsRepository
import com.example.newsapp.core.model.NewsItem
import javax.inject.Inject

class GetNewsDetailsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(newsLink: String): SealedResult<NewsItem?, DataError> {
        return repository.getNewsDetails(newsLink)
    }
}
