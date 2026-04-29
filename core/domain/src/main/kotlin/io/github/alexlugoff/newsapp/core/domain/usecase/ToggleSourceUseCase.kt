package io.github.alexlugoff.newsapp.core.domain.usecase

import io.github.alexlugoff.newsapp.core.domain.repository.NewsRepository
import javax.inject.Inject

class ToggleSourceUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(sourceId: Int, isEnabled: Boolean) =
        repository.toggleSource(sourceId, isEnabled)
}
