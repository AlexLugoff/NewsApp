package com.example.newsapp.data.models

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

// Корневой элемент документа
@Xml(name = "rss")
data class RssFeedDto(
    @Element(name = "channel")
    val channel: RssChannelDto? = null
)