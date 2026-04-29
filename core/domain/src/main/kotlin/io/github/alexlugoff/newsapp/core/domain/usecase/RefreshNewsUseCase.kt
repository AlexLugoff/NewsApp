package io.github.alexlugoff.newsapp.core.domain.usecase

import io.github.alexlugoff.newsapp.core.common.error.DataError
import io.github.alexlugoff.newsapp.core.common.result.SealedResult
import io.github.alexlugoff.newsapp.core.domain.repository.NewsRepository
import javax.inject.Inject

class RefreshNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {

    suspend operator fun invoke(): SealedResult<Unit, DataError> {
        return repository.refreshNews()
    }
}
