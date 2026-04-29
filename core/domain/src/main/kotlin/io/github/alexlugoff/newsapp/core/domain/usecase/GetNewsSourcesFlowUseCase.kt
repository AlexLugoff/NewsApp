package io.github.alexlugoff.newsapp.core.domain.usecase

import io.github.alexlugoff.newsapp.core.common.dispatchers.AppDispatchers
import io.github.alexlugoff.newsapp.core.domain.repository.NewsRepository
import io.github.alexlugoff.newsapp.core.model.NewsSourceItem
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
