package com.example.newsapp.data.mappers

import com.example.newsapp.core.model.NewsSourceItem
import com.example.newsapp.data.db.entities.NewsSourceEntity

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