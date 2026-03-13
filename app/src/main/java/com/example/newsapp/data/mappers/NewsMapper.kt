package com.example.newsapp.data.mappers

import android.text.Html
import android.util.Log
import com.example.newsapp.asDateTimeString
import com.example.newsapp.data.DateParser
import com.example.newsapp.data.db.entities.NewsItemEntity
import com.example.newsapp.data.models.RssFeedDto
import com.example.newsapp.domain.models.NewsItem
import com.example.newsapp.toSpannedHtml

// DTO -> Entity
fun RssFeedDto.toEntityList(): List<NewsItemEntity> {
    return this.channel?.newsItems?.mapNotNull { itemDto ->
        if (itemDto.link.isBlank()) {
            Log.w("RssFeedMapping", "Skipping item: missing link. Title: ${itemDto.title}")
            return@mapNotNull null
        }
        val dateLong = DateParser.parseToLong(itemDto.pubDate) ?: return@mapNotNull null

        with(itemDto) {
            NewsItemEntity(
                link = link,
                title = title,
                description = description,
                imageUrl = enclosure?.url,
                pubDate = dateLong
            )
        }
    } ?: emptyList()
}

// Entity -> Domain
fun NewsItemEntity.toDomain(): NewsItem {
    return NewsItem(
        id = this.link,
        title = this.title,
        description = this.description.toSpannedHtml(),
        imageUrl = this.imageUrl,
        link = this.link,
        formattedDate = this.pubDate.asDateTimeString
    )
}

// List<Entity> -> List<Domain>
fun List<NewsItemEntity>.toDomainList(): List<NewsItem> = this.map { it.toDomain() }