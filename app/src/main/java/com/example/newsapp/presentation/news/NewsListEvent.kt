package com.example.newsapp.presentation.news

sealed class NewsListEvent {
    class NavigateToNewsDetails(val newsLink: String) : NewsListEvent()
}