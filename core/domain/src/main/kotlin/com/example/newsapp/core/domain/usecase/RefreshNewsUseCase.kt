package com.example.newsapp.core.domain.usecase

import com.example.newsapp.core.common.DataError
import com.example.newsapp.core.common.SealedResult
import com.example.newsapp.core.domain.repository.NewsRepository
import javax.inject.Inject

class RefreshNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {

    suspend operator fun invoke(): SealedResult<Unit, DataError> {
        return repository.refreshNews()
    }
}
