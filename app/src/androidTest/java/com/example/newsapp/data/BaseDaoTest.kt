package com.example.newsapp.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.newsapp.data.db.AppDatabase
import org.junit.After
import org.junit.Before
import java.io.IOException

abstract class BaseDaoTest {

    protected lateinit var database: AppDatabase

    @Before
    open fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Создаем базу в оперативной памяти (удаляется после теста)
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    @Throws(IOException::class)
    open fun closeDb() {
        database.close()
    }
}