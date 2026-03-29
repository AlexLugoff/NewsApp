package com.example.newsapp.core.data.mappers

import com.example.newsapp.core.database.entities.NewsSourceEntity
import com.example.newsapp.core.model.NewsSourceItem

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
