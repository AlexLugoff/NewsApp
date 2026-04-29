package io.github.alexlugoff.newsapp.core.network.di

import io.github.alexlugoff.newsapp.core.network.NewsRemoteDataSource
import io.github.alexlugoff.newsapp.core.network.NewsRemoteDataSourceImpl
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
}
