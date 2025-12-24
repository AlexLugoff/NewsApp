package com.example.newsapp.ui.news

import com.example.newsapp.domain.models.NewsItem
import com.example.newsapp.ui.UniversalText

sealed class NewsListViewState {
    object Loading : NewsListViewState()
    data class Success(val news: List<NewsItem>) : NewsListViewState()
    data class Error(val message: UniversalText) : NewsListViewState()
}