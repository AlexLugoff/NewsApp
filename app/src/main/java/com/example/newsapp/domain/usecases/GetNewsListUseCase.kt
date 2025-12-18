package com.example.newsapp.domain.usecases

import com.example.newsapp.SealedResult
import com.example.newsapp.data.exception.DataError
import com.example.newsapp.domain.models.NewsItem
import com.example.newsapp.domain.repository.NewsRepository
import javax.inject.Inject

class GetNewsListUseCase @Inject constructor(
    private val repository: NewsRepository
) {

    suspend operator fun invoke(): SealedResult<List<NewsItem>, DataError> {
        return repository.getNews()
    }
}