package io.github.alexlugoff.newsapp.feature.details.presentation

sealed class NewsDetailsEvent {
    class GoToBrowser(val url: String) : NewsDetailsEvent()
    class ShareNews(val url: String) : NewsDetailsEvent()
}
