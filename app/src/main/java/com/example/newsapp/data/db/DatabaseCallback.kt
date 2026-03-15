package com.example.newsapp.data.db

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

class DatabaseCallback() : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        db.execSQL(
            """
            INSERT INTO news_sources (name, url, isEnabled) VALUES 
            ('Habr', 'https://habr.com/ru/rss/articles/', 0),
            ('Lenta.ru', 'https://lenta.ru/rss/', 1),
            ('РБК', 'https://rssexport.rbc.ru/rbcnews/news/30/full.rss', 1)
            """.trimIndent()
        )
    }
}