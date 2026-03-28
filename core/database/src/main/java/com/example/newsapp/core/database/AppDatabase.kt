package com.example.newsapp.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsapp.core.database.dao.NewsDao
import com.example.newsapp.core.database.dao.NewsSourceDao
import com.example.newsapp.core.database.entities.NewsItemEntity
import com.example.newsapp.core.database.entities.NewsSourceEntity

@Database(
    entities = [NewsItemEntity::class, NewsSourceEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao
    abstract fun newsSourceDao(): NewsSourceDao
}
