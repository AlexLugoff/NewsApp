package com.example.newsapp.data

import com.example.newsapp.data.models.RssFeedDto
import retrofit2.http.GET
import retrofit2.http.Url
import java.util.concurrent.TimeUnit

interface RssApiService {

    companion object {

        const val CONNECT_TIMEOUT = 30L
        const val READ_TIMEOUT = 30L
        const val WRITE_TIMEOUT = 30L

        val timeUnit = TimeUnit.SECONDS
    }

     @GET
     suspend fun getNews(@Url url: String): RssFeedDto
}