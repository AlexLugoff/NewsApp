package io.github.alexlugoff.newsapp.feature.news.presentation

import io.github.alexlugoff.newsapp.core.model.NewsItem
import io.github.alexlugoff.newsapp.core.ui.util.UiText

sealed class NewsListViewState {
    object Loading : NewsListViewState()
    data class Success(val news: List<NewsItem>, val isRefreshing: Boolean = false) :
        NewsListViewState()

    data class Error(val message: UiText) : NewsListViewState()

    val currentNews: List<NewsItem>
        get() = when (this) {
            is Success -> news
            else -> emptyList()
        }
}
