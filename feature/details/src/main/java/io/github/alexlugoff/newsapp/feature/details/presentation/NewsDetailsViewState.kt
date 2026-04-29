package io.github.alexlugoff.newsapp.feature.details.presentation

import io.github.alexlugoff.newsapp.core.model.NewsItem
import io.github.alexlugoff.newsapp.core.ui.util.UiText

sealed class NewsDetailsViewState {
    object Loading : NewsDetailsViewState()
    data class Success(val newsItem: NewsItem) : NewsDetailsViewState()
    data class Error(val message: UiText) : NewsDetailsViewState()
}
