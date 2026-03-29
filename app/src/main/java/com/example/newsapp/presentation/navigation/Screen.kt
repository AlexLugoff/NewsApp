package com.example.newsapp.presentation.navigation

import android.net.Uri

sealed class Screen(val route: String) {
    object NewsList : Screen("news_list")
    object NewsDetails : Screen("news_details?url={url}") {
        fun createRoute(url: String) = "news_details?url=${Uri.encode(url)}"
    }
}