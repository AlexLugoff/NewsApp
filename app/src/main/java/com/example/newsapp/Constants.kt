package com.example.newsapp

import kotlin.time.Duration.Companion.days

const val BASE_URL = "https://lenta.ru"

const val DATABASE_NAME = "app_news_database.db"

const val TIMEOUT_PAUSE: Long = 5000
val NEWS_EXPIRATION_THRESHOLD = 3.days