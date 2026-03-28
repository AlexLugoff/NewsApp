package com.example.newsapp.di.module

import android.content.Context
import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import coil3.util.Logger
import com.example.newsapp.BuildConfig
import com.example.newsapp.core.common.util.COIL_DISK_CACHE_DIRECTORY_NAME
import com.example.newsapp.core.common.util.COIL_DISK_CACHE_SIZE_PERCENT
import com.example.newsapp.core.common.util.COIL_MEMORY_CACHE_SIZE_PERCENT
import com.example.newsapp.core.common.dispatchers.AppDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okio.Path.Companion.toOkioPath
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDispatchers(): AppDispatchers = AppDispatchers(
        main = Dispatchers.Main,
        default = Dispatchers.Default,
        io = Dispatchers.IO,
        unconfined = Dispatchers.Unconfined
    )

    @Provides
    @Singleton
    fun provideImageLoader(
        @ApplicationContext context: Context,
        okHttpClient: OkHttpClient
    ): ImageLoader = ImageLoader.Builder(context)
        .memoryCache {
            MemoryCache.Builder()
                .maxSizePercent(context, COIL_MEMORY_CACHE_SIZE_PERCENT)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(context.cacheDir.resolve(COIL_DISK_CACHE_DIRECTORY_NAME).toOkioPath())
                .maxSizePercent(COIL_DISK_CACHE_SIZE_PERCENT)
                .build()
        }
        .components {
            add(OkHttpNetworkFetcherFactory(callFactory = { okHttpClient }))
        }
        .crossfade(true)
        .apply {
            if (BuildConfig.DEBUG) {
                logger(DebugLogger(Logger.Level.Verbose))
            }
        }
        .build()
}