package com.example.newsapp.core.network.di

import com.example.newsapp.core.common.CONNECT_TIMEOUT
import com.example.newsapp.core.common.READ_TIMEOUT
import com.example.newsapp.core.common.WRITE_TIMEOUT
import com.example.newsapp.core.common.timeUnit
import com.example.newsapp.core.network.BuildConfig
import com.prof18.rssparser.RssParser
import com.prof18.rssparser.RssParserBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRssParser(): RssParser {
        return RssParserBuilder().build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, timeUnit)
            .writeTimeout(WRITE_TIMEOUT, timeUnit)
            .readTimeout(READ_TIMEOUT, timeUnit)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

}
