package com.example.newsapp.data.models

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "item")
data class NewsItemDto(
    @PropertyElement(name = "title")
    val title: String = "",

    @PropertyElement(name = "link")
    val link: String = "",

    @PropertyElement(name = "description")
    val description: String = "",

    @Element(name = "enclosure")
    val enclosure: EnclosureDto? = null,

    @PropertyElement(name = "pubDate")
    val pubDate: String = "",

    @PropertyElement(name = "category")
    val category: String = ""
)