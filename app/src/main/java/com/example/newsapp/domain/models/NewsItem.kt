package com.example.newsapp.domain.models

import android.text.Spanned

data class NewsItem(
    val id: String, // Уникальный ID, может быть сгенерирован из link
    val title: String,
    val description: Spanned,
    val imageUrl: String?,
    val link: String,
    val formattedDate: String
)