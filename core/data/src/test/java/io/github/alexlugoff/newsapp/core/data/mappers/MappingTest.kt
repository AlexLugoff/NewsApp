package io.github.alexlugoff.newsapp.core.data.mappers

import com.prof18.rssparser.model.RssChannel
import com.prof18.rssparser.model.RssItem
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MappingTest {
    @Test
    fun `toEntityList - when image is present - maps imageUrl correctly`() {
        val rssItem = RssItem(
            link = "https://news.com/1",
            title = "Title",
            image = "https://image.com/1.jpg",
            pubDate = "Tue, 15 Jul 2025 10:00:00 GMT",
            description = "Description",
            content = null,
            author = null,
            categories = emptyList(),
            guid = null,
            audio = null,
            video = null,
            sourceName = null,
            sourceUrl = null,
            itunesItemData = null,
            commentsUrl = null,
            youtubeItemData = null,
            rawEnclosure = null
        )
        
        val channel = RssChannel(
            title = "Channel",
            link = "https://news.com",
            description = "Description",
            image = null,
            items = listOf(rssItem),
            lastBuildDate = null,
            updatePeriod = null,
            itunesChannelData = null,
            youtubeChannelData = null
        )

        val entities = channel.toEntityList()
        assertEquals("https://image.com/1.jpg", entities.first().imageUrl)
    }
}
