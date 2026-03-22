package com.example.newsapp.data.mappers

import com.example.newsapp.data.DateParser
import com.example.newsapp.data.db.entities.NewsItemEntity
import com.example.newsapp.domain.models.NewsItem
import com.example.newsapp.extensions.asDateTimeString
import com.example.newsapp.extensions.toSpannedHtml
import com.prof18.rssparser.model.RssChannel
import timber.log.Timber

// DTO -> Entity
fun RssChannel.toEntityList(): List<NewsItemEntity> {
    return this.items.mapNotNull { itemDto ->
        if (itemDto.link.isNullOrBlank()) {
            Timber.w("Skipping item: missing link. Title: ${itemDto.title}")
            return@mapNotNull null
        }
        val dateLong = DateParser.parseToLong(itemDto.pubDate) ?: return@mapNotNull null

        with(itemDto) {
            NewsItemEntity(
                title = title ?: "Без заголовка",
                link = link!!,
                description = description ?: "",
                imageUrl = image,
                pubDate = dateLong,
            )
        }
    }
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