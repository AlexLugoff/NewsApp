package com.example.newsapp.presentation.news_details

sealed class NewsDetailsEvent {
    class GoToBrowser(val url: String) : NewsDetailsEvent()
}