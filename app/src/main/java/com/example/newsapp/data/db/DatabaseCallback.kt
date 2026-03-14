package com.example.newsapp.data.db

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.newsapp.data.db.entities.NewsSourceEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Provider

class DatabaseCallback(
    private val scope: CoroutineScope,
    private val daoProvider: Provider<NewsSourceDao>
) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        scope.launch {
            daoProvider.get().insertSources(
                listOf(
                    NewsSourceEntity(
                        name = "Habr",
                        url = "https://habr.com/ru/rss/articles/",
                        isEnabled = true
                    ),
                    NewsSourceEntity(
                        name = "Lenta.ru",
                        url = "https://lenta.ru/rss/",
                        isEnabled = true
                    ),
                    NewsSourceEntity(
                        name = "РБК",
                        url = "https://rssexport.rbc.ru/rbcnews/news/30/full.rss",
                        isEnabled = false
                    )
                )
            )
        }
    }
}