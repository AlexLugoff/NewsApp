package io.github.alexlugoff.newsapp.core.domain.usecase

import io.github.alexlugoff.newsapp.core.common.error.DataError
import io.github.alexlugoff.newsapp.core.common.result.SealedResult
import io.github.alexlugoff.newsapp.core.domain.repository.NewsRepository
import io.github.alexlugoff.newsapp.core.model.NewsItem
import javax.inject.Inject

class GetNewsDetailsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(newsLink: String): SealedResult<NewsItem?, DataError> {
        return repository.getNewsDetails(newsLink)
    }
}
