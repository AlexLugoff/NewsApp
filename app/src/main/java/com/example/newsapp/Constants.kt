package com.example.newsapp

import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.days

const val CONNECT_TIMEOUT = 30L
const val READ_TIMEOUT = 30L
const val WRITE_TIMEOUT = 30L

val timeUnit = TimeUnit.SECONDS

const val DATABASE_NAME = "app_news_database.db"

const val DATABASE_FILE_PATH = "database/seed_database.db"

const val TIMEOUT_PAUSE: Long = 5000
val NEWS_EXPIRATION_THRESHOLD = 3.days

const val INTENT_TYPE = "text/plain"