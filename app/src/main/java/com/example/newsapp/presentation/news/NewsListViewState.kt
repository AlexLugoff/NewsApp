package com.example.newsapp.presentation.news

import com.example.newsapp.domain.models.NewsItem
import com.example.newsapp.presentation.UiText

sealed class NewsListViewState {
    object Loading : NewsListViewState()
    data class Success(val news: List<NewsItem>, val isRefreshing: Boolean = false) : NewsListViewState()
    data class Error(val message: UiText) : NewsListViewState()
    val currentNews: List<NewsItem>
        get() = when (this) {
            is Success -> news
            else -> emptyList()
        }
}