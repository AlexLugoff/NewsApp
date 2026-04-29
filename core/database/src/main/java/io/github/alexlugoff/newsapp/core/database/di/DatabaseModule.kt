package io.github.alexlugoff.newsapp.core.database.di

import android.content.Context
import androidx.room.Room
import io.github.alexlugoff.newsapp.core.common.util.DATABASE_FILE_PATH
import io.github.alexlugoff.newsapp.core.common.util.DATABASE_NAME
import io.github.alexlugoff.newsapp.core.database.AppDatabase
import io.github.alexlugoff.newsapp.core.database.dao.NewsDao
import io.github.alexlugoff.newsapp.core.database.dao.NewsSourceDao
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
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    fun provideNewsDao(db: AppDatabase): NewsDao = db.newsDao()

    @Provides
    fun provideNewsSourceDao(db: AppDatabase): NewsSourceDao = db.newsSourceDao()

}
