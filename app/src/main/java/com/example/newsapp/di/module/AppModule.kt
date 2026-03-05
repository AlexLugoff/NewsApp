package com.example.newsapp.di.module

import com.example.newsapp.AppDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDispatchers(): AppDispatchers {
        return AppDispatchers(
            main = Dispatchers.Main,
            default = Dispatchers.Default,
            io = Dispatchers.IO,
            unconfined = Dispatchers.Unconfined
        )
    }
}