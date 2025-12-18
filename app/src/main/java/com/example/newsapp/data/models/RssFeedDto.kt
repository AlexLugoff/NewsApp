package com.example.newsapp.data.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

// Корневой элемент документа
@Root(name = "rss", strict = false)
data class RssFeedDto @JvmOverloads constructor(
    @field:Element(name = "channel")
    @param:Element(name = "channel")
    val channel: RssChannelDto? = null
)