package com.example.newsapp.di.module

import android.content.Context
import androidx.room.Room
import com.example.newsapp.DATABASE_FILE_PATH
import com.example.newsapp.DATABASE_NAME
import com.example.newsapp.data.db.AppDatabase
import com.example.newsapp.data.db.NewsDao
import com.example.newsapp.data.db.NewsSourceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        )
            .createFromAsset(DATABASE_FILE_PATH)
            .build()
    }

    @Provides
    fun provideNewsDao(db: AppDatabase): NewsDao = db.newsDao()

    @Provides
    fun provideNewsSourceDao(db: AppDatabase): NewsSourceDao = db.newsSourceDao()

}