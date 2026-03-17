package com.example.newsapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsapp.data.db.entities.NewsItemEntity
import com.example.newsapp.data.db.entities.NewsSourceEntity

@Database(
    entities = [NewsItemEntity::class, NewsSourceEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao
    abstract fun newsSourceDao(): NewsSourceDao
}