package com.example.newsapp.ui.news_details

import com.example.newsapp.domain.models.NewsItem

sealed class NewsDetailsViewState {
    object Loading : NewsDetailsViewState()
    data class Success(val newsItem: NewsItem) : NewsDetailsViewState()
    data class Error(val message: String) : NewsDetailsViewState()
}