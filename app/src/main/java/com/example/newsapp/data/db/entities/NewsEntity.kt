package com.example.newsapp.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsEntity(
    // link - уникален для каждой новости в RSS
    @PrimaryKey val link: String,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val pubDate: Long // Храним дату в виде Long (UNIX timestamp) для сортировки
)