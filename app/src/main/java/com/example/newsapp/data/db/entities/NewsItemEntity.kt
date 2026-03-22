package com.example.newsapp.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsItemEntity(
    @PrimaryKey val link: String,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val pubDate: Long,
)