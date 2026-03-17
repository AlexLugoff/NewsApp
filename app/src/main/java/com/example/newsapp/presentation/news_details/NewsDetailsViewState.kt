package com.example.newsapp.presentation.news_details

import com.example.newsapp.domain.models.NewsItem
import com.example.newsapp.presentation.UiText

sealed class NewsDetailsViewState {
    object Loading : NewsDetailsViewState()
    data class Success(val newsItem: NewsItem) : NewsDetailsViewState()
    data class Error(val message: UiText) : NewsDetailsViewState()
}