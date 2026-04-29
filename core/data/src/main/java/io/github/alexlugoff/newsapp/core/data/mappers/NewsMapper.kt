package io.github.alexlugoff.newsapp.core.data.mappers

import io.github.alexlugoff.newsapp.core.common.util.asDateTimeString
import io.github.alexlugoff.newsapp.core.model.NewsItem
import io.github.alexlugoff.newsapp.core.data.DateParser
import io.github.alexlugoff.newsapp.core.database.entities.NewsItemEntity
import com.prof18.rssparser.model.RssChannel
import timber.log.Timber

// DTO -> Entity
fun RssChannel.toEntityList(): List<NewsItemEntity> {
    return this.items.mapNotNull { itemDto ->
        val link = itemDto.link
        if (link.isNullOrBlank()) {
            Timber.w("Skipping item: missing link. Title: ${itemDto.title}")
            return@mapNotNull null
        }
        val dateLong = DateParser.parseToLong(itemDto.pubDate) ?: return@mapNotNull null

        NewsItemEntity(
            title = itemDto.title.orEmpty(),
            link = link,
            description = itemDto.description.orEmpty(),
            imageUrl = itemDto.image,
            pubDate = dateLong,
        )
    }
}

// Entity -> Domain
fun NewsItemEntity.toDomain(): NewsItem {
    return NewsItem(
        id = this.link,
        title = this.title,
        description = this.description,
        imageUrl = this.imageUrl,
        link = this.link,
        formattedDate = this.pubDate.asDateTimeString
    )
}

// List<Entity> -> List<Domain>
fun List<NewsItemEntity>.toDomainList(): List<NewsItem> = this.map { it.toDomain() }
