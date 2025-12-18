package com.example.newsapp.data.models

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root

@Root(name = "enclosure", strict = false)
data class EnclosureDto @JvmOverloads constructor(
    @field:Attribute(name = "url")
    @param:Attribute(name = "url")
    val url: String = ""
)