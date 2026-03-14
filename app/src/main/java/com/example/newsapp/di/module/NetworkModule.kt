package com.example.newsapp.di.module

import com.example.newsapp.BASE_URL
import com.example.newsapp.data.RssApiService
import com.squareup.picasso.BuildConfig
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(RssApiService.CONNECT_TIMEOUT, RssApiService.timeUnit)
            .writeTimeout(RssApiService.WRITE_TIMEOUT, RssApiService.timeUnit)
            .readTimeout(RssApiService.READ_TIMEOUT, RssApiService.timeUnit)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideBaseApi(
        okHttpClient: OkHttpClient,
        tikXmlConverterFactory: TikXmlConverterFactory
    ): RssApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(tikXmlConverterFactory)
            .client(okHttpClient)
            .build()
            .create(RssApiService::class.java)
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

    @Provides
    @Singleton
    fun provideTikXmlConverterFactory(): TikXmlConverterFactory =
        TikXmlConverterFactory.create(
            TikXml.Builder()
                .exceptionOnUnreadXml(false)
                .build()
        )

}