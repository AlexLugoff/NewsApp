package com.example.newsapp

import com.example.newsapp.data.mappers.toEntityList
import com.example.newsapp.data.models.EnclosureDto
import com.example.newsapp.data.models.NewsItemDto
import com.example.newsapp.data.models.RssChannelDto
import com.example.newsapp.data.models.RssFeedDto
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MappingTest {
    @Test
    fun `dtoToEntity - when enclosure is present - maps imageUrl correctly`() {
        val dto = RssFeedDto(
            channel = RssChannelDto(
                newsItems = listOf(
                    NewsItemDto(
                        link = "link",
                        title = "Title",
                        enclosure = EnclosureDto(url = "https://image.com/1.jpg"),
                        pubDate = "Tue, 15 Jul 2025 10:00:00 GMT"
                    )
                )
            )
        )

        val entities = dto.toEntityList()
        assertEquals("https://image.com/1.jpg", entities.first().imageUrl)
    }
}