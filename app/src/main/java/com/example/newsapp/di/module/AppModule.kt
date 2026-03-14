package com.example.newsapp.di.module

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.example.newsapp.AppDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
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

    @Provides
    @Singleton
    fun provideImageLoader(
        @ApplicationContext context: Context,
        okHttpClient: OkHttpClient
    ): ImageLoader {
        return ImageLoader.Builder(context)
            .okHttpClient(okHttpClient)
            .crossfade(true)
            .respectCacheHeaders(false) // Позволяет Coil кешировать картинки, даже если сервер не прислал заголовки
            .memoryCache {
                MemoryCache.Builder(context)
                    .maxSizePercent(0.20) // Резервируем 20% оперативной памяти под картинки
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02) // Резервируем место на диске
                    .build()
            }
            .build()
    }
}