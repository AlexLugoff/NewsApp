package io.github.alexlugoff.newsapp.core.data.mappers

import io.github.alexlugoff.newsapp.core.database.entities.NewsSourceEntity
import io.github.alexlugoff.newsapp.core.model.NewsSourceItem

fun NewsSourceEntity.toDomain(): NewsSourceItem {
    return NewsSourceItem(
        id = this.id,
        name = this.name,
        url = this.url,
        isEnabled = this.isEnabled
    )
}

fun NewsSourceItem.toEntity(): NewsSourceEntity {
    return NewsSourceEntity(
        id = this.id,
        name = this.name,
        url = this.url,
        isEnabled = this.isEnabled
    )
}
