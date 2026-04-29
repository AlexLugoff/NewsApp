package io.github.alexlugoff.newsapp.core.model

data class NewsItem(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val link: String,
    val formattedDate: String
)