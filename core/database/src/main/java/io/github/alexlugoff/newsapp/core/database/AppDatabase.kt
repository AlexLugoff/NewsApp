package io.github.alexlugoff.newsapp.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.alexlugoff.newsapp.core.database.dao.NewsDao
import io.github.alexlugoff.newsapp.core.database.dao.NewsSourceDao
import io.github.alexlugoff.newsapp.core.database.entities.NewsItemEntity
import io.github.alexlugoff.newsapp.core.database.entities.NewsSourceEntity

@Database(
    entities = [NewsItemEntity::class, NewsSourceEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao
    abstract fun newsSourceDao(): NewsSourceDao
}
