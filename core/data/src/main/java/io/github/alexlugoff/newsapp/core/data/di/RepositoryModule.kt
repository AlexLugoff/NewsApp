package io.github.alexlugoff.newsapp.core.data.di

import io.github.alexlugoff.newsapp.core.data.repository.NewsRepositoryImpl
import io.github.alexlugoff.newsapp.core.domain.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNewsRepository(
        impl: NewsRepositoryImpl
    ): NewsRepository
}
