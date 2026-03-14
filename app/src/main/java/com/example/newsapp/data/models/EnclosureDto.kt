package com.example.newsapp.data.models

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "enclosure")
data class EnclosureDto(
    @Attribute(name = "url")
    val url: String = "",

    @Attribute(name = "type")
    val type: String = ""
)