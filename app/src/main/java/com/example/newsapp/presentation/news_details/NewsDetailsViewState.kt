package com.example.newsapp.presentation.news_details

import com.example.newsapp.core.common.UiText
import com.example.newsapp.core.model.NewsItem

sealed class NewsDetailsViewState {
    object Loading : NewsDetailsViewState()
    data class Success(val newsItem: NewsItem) : NewsDetailsViewState()
    data class Error(val message: UiText) : NewsDetailsViewState()
}