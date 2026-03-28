package com.example.newsapp.presentation.news

import com.example.newsapp.core.ui.util.UiText

sealed class NewsListEvent {
    data class ShowErrorMessage(val message: UiText) : NewsListEvent()
}