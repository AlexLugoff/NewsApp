package com.example.newsapp.di

import com.example.newsapp.di.module.RepositoryModule
import com.example.newsapp.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import io.mockk.mockk
import javax.inject.Singleton

@Module
@UninstallModules(RepositoryModule::class)
@InstallIn(SingletonComponent::class)
object TestRepositoryModule {

    @Provides
    @Singleton
    fun provideMockNewsRepository(): NewsRepository {
        return mockk(relaxed = true)
        // relaxed = true делает мок более гибким, но в реальных тестах
        // лучше использовать coEvery для точного контроля.
    }
}