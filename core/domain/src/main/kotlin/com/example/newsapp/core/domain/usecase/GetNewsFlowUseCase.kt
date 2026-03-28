package com.example.newsapp.core.domain.usecase

import com.example.newsapp.core.common.AppDispatchers
import com.example.newsapp.core.domain.repository.NewsRepository
import com.example.newsapp.core.model.NewsItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetNewsFlowUseCase @Inject constructor(
    private val repository: NewsRepository,
    private val dispatchers: AppDispatchers,
) {
    operator fun invoke(): Flow<List<NewsItem>> {
        return repository.getNewsFlow().flowOn(dispatchers.io)
    }
}
