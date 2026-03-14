package com.example.newsapp.domain.usecases

import com.example.newsapp.domain.models.NewsSourceItem
import com.example.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNewsSourcesUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke(): Flow<List<NewsSourceItem>> {
        return repository.getNewsSources()
    }
}