package com.example.newsapp.domain.models

data class NewsSourceItem(
    val id: Int,
    val name: String,
    val url: String,
    val isEnabled: Boolean
)