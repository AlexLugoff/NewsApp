package com.example.newsapp.di.module

import com.example.newsapp.BASE_URL
import com.example.newsapp.data.RssApiService
import com.squareup.picasso.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideBaseApi(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        simpleXmlConverterFactory: SimpleXmlConverterFactory,
    ): RssApiService {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(RssApiService.CONNECT_TIMEOUT, RssApiService.timeUnit)
            .writeTimeout(RssApiService.WRITE_TIMEOUT, RssApiService.timeUnit)
            .readTimeout(RssApiService.READ_TIMEOUT, RssApiService.timeUnit)
            .addInterceptor(httpLoggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(simpleXmlConverterFactory)
            .client(okHttpClient)
            .build()
        return retrofit.create(RssApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
    }

    @Provides
    @Singleton
    fun provideSimpleXmlConverterFactory(): SimpleXmlConverterFactory =
        SimpleXmlConverterFactory.create()

}