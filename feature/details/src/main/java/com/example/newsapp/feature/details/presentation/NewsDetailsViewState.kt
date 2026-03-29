package com.example.newsapp.feature.details.presentation

import com.example.newsapp.core.model.NewsItem
import com.example.newsapp.core.ui.util.UiText

sealed class NewsDetailsViewState {
    object Loading : NewsDetailsViewState()
    data class Success(val newsItem: NewsItem) : NewsDetailsViewState()
    data class Error(val message: UiText) : NewsDetailsViewState()
}
