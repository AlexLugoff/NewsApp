package com.example.newsapp.di.module

import com.example.newsapp.data.datasource.local.NewsLocalDataSource
import com.example.newsapp.data.datasource.local.NewsLocalDataSourceImpl
import com.example.newsapp.data.datasource.remote.NewsRemoteDataSource
import com.example.newsapp.data.datasource.remote.NewsRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindNewsRemoteDataSource(
        impl: NewsRemoteDataSourceImpl
    ): NewsRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindNewsLocalDataSource(
        impl: NewsLocalDataSourceImpl
    ): NewsLocalDataSource
}