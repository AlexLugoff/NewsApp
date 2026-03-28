package com.example.newsapp.core.domain.usecase

import com.example.newsapp.core.domain.repository.NewsRepository
import javax.inject.Inject

class ClearOldNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke() {
        repository.clearOldNews()
    }
}
