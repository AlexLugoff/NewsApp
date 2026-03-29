package com.example.newsapp.feature.news.presentation

import com.example.newsapp.core.ui.util.UiText

sealed class NewsListEvent {
    data class ShowErrorMessage(val message: UiText) : NewsListEvent()
}
