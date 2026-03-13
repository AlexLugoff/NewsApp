package com.example.newsapp.di.module

import android.content.Context
import androidx.room.Room
import com.example.newsapp.DATABASE_NAME
import com.example.newsapp.data.db.AppDatabase
import com.example.newsapp.data.db.DatabaseCallback
import com.example.newsapp.data.db.NewsDao
import com.example.newsapp.data.db.NewsSourceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        sourceDaoProvider: Provider<NewsSourceDao>
    ): AppDatabase {
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        )
            .addCallback(DatabaseCallback(scope, sourceDaoProvider))
            .build()
    }

    @Provides
    fun provideNewsDao(db: AppDatabase): NewsDao = db.newsDao()

    @Provides
    fun provideNewsSourceDao(db: AppDatabase): NewsSourceDao = db.newsSourceDao()

}