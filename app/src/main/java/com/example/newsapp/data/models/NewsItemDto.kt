package com.example.newsapp.data.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "item", strict = false)
data class NewsItemDto constructor(
    @field:Element(name = "title")
    @param:Element(name = "title")
    val title: String = "",

    @field:Element(name = "link")
    @param:Element(name = "link")
    val link: String = "",

    @field:Element(name = "description")
    @param:Element(name = "description")
    val description: String = "",

    @field:Element(name = "enclosure", required = false)
    @param:Element(name = "enclosure", required = false)
    val enclosure: EnclosureDto? = null,

    @field:Element(name = "pubDate") // Дата публикации
    @param:Element(name = "pubDate")
    val pubDate: String = ""
)