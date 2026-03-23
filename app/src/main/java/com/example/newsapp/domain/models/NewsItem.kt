package com.example.newsapp.domain.models

import androidx.compose.runtime.Immutable

@Immutable
data class NewsItem(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val link: String,
    val formattedDate: String
)