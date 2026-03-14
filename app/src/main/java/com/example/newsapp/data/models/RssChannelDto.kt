package com.example.newsapp.data.models

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "channel")
data class RssChannelDto(
    @PropertyElement(name = "title")
    val title: String = "",
    @Element(name = "item")
    val newsItems: List<NewsItemDto> = emptyList()
)