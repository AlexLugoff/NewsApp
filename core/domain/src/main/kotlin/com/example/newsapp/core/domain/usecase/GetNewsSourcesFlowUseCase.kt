package com.example.newsapp.core.domain.usecase

import com.example.newsapp.core.common.AppDispatchers
import com.example.newsapp.core.domain.repository.NewsRepository
import com.example.newsapp.core.model.NewsSourceItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetNewsSourcesFlowUseCase @Inject constructor(
    private val repository: NewsRepository,
    private val dispatchers: AppDispatchers
) {
    operator fun invoke(): Flow<List<NewsSourceItem>> {
        return repository.getNewsSources().flowOn(dispatchers.io)
    }
}
