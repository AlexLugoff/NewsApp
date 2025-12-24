package com.example.newsapp.ui.news_details

sealed class NewsDetailsEvent {
    class GoToBrowser(val url: String) : NewsDetailsEvent()
}