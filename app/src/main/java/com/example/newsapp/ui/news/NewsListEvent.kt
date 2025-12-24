package com.example.newsapp.ui.news

sealed class NewsListEvent {
    class NavigateToNewsDetails(val newsLink: String) : NewsListEvent()
}