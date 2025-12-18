package com.example.newsapp.data.mappers

import com.example.newsapp.data.db.entities.NewsEntity
import com.example.newsapp.data.models.RssFeedDto
import com.example.newsapp.domain.models.NewsItem
import java.text.SimpleDateFormat
import java.util.Locale

// ISO 8601 формат для RSS (например, Mon, 11 Nov 2024 10:00:00 +0300)
private val rssDateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US)

// --- DTO -> Entity ---
fun RssFeedDto.toEntityList(): List<NewsEntity> {
    return this.channel?.newsItems?.mapNotNull { itemDto ->
        val dateLong = try {
            rssDateFormat.parse(itemDto.pubDate)?.time ?: 0L
        } catch (e: Exception) {
            0L
        }

        if (itemDto.link.isEmpty()) return@mapNotNull null

        NewsEntity(
            link = itemDto.link,
            title = itemDto.title,
            description = itemDto.description,
            imageUrl = itemDto.enclosure?.url,
            pubDate = dateLong
        )
    } ?: emptyList()
}

// И Entity -> Domain
fun NewsEntity.toDomain(): NewsItem {
    return NewsItem(
        id = this.link,
        title = this.title,
        description = this.description,
        imageUrl = this.imageUrl,
        link = this.link,
        date = this.pubDate
    )
}

// --- List<Entity> -> List<Domain> ---
fun List<NewsEntity>.toDomainList(): List<NewsItem> = this.map { it.toDomain() }