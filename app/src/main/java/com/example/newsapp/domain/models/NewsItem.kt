package com.example.newsapp.domain.models

data class NewsItem(
    val id: String, // Уникальный ID, может быть сгенерирован из link
    val title: String,
    val description: String,
    val imageUrl: String?,
    val link: String,
    val date: Long
)