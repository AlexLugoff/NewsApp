package com.example.newsapp.data

import com.example.newsapp.data.models.RssFeedDto
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

interface RssApiService {

    companion object {

        const val CONNECT_TIMEOUT = 30L
        const val READ_TIMEOUT = 30L
        const val WRITE_TIMEOUT = 30L

        val timeUnit = TimeUnit.SECONDS
    }

    @GET("/rss")
    suspend fun getNews(): RssFeedDto

    // TODO использовать динамический URL, чтобы не "зашивать" его в код сервиса, если источников может быть несколько.
    // @GET
    // suspend fun getNewsFeed(@Url url: String): RssFeedDto
}