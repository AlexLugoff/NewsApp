package com.example.newsapp.domain.usecases

import com.example.newsapp.core.common.SealedResult
import com.example.newsapp.data.exception.DataError
import com.example.newsapp.domain.repository.NewsRepository
import javax.inject.Inject

class RefreshNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {

    suspend operator fun invoke(): SealedResult<Unit, DataError> {
        return repository.refreshNews()
    }
}