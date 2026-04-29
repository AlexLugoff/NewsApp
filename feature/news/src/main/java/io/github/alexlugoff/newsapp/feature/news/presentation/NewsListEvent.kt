package io.github.alexlugoff.newsapp.feature.news.presentation

import io.github.alexlugoff.newsapp.core.ui.util.UiText

sealed class NewsListEvent {
    data class ShowErrorMessage(val message: UiText) : NewsListEvent()
}
