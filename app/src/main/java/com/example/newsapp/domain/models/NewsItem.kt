package com.example.newsapp.domain.models

import android.text.Spanned
import androidx.compose.runtime.Immutable

@Immutable
data class NewsItem(
    val id: String,
    val title: String,
    val description: Spanned,
    val imageUrl: String?,
    val link: String,
    val formattedDate: String
)