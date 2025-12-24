package com.example.newsapp.ui.news_details

import com.example.newsapp.domain.models.NewsItem
import com.example.newsapp.ui.UniversalText

sealed class NewsDetailsViewState {
    object Loading : NewsDetailsViewState()
    data class Success(val newsItem: NewsItem) : NewsDetailsViewState()
    data class Error(val message: UniversalText) : NewsDetailsViewState()
}