package io.github.alexlugoff.newsapp.core.database.di

import io.github.alexlugoff.newsapp.core.database.datasource.NewsLocalDataSource
import io.github.alexlugoff.newsapp.core.database.datasource.NewsLocalDataSourceImpl
import io.github.alexlugoff.newsapp.core.database.datasource.NewsSourceLocalDataSource
import io.github.alexlugoff.newsapp.core.database.datasource.NewsSourceLocalDataSourceImpl
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
    abstract fun bindNewsLocalDataSource(
        impl: NewsLocalDataSourceImpl
    ): NewsLocalDataSource

    @Binds
    @Singleton
    abstract fun bindNewsSourceLocalDataSource(
        impl: NewsSourceLocalDataSourceImpl
    ): NewsSourceLocalDataSource
}
