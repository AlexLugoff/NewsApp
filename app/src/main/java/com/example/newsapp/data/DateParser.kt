package com.example.newsapp.data

import android.util.Log
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateParser {

    private const val TAG = "DateParser"
    private val formatters = listOf(
        DateTimeFormatter.RFC_1123_DATE_TIME,
        DateTimeFormatter.ISO_DATE_TIME,
        DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.US),
        DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss Z", Locale.US),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US)
    )

    fun parseToLong(dateString: String?): Long? {
        if (dateString.isNullOrBlank()) {
            Log.w(TAG, "Empty or null date string received")
            return null
        }

        val cleanedDate = dateString.trim()

        for (formatter in formatters) {
            try {
                return ZonedDateTime.parse(cleanedDate, formatter)
                    .toInstant()
                    .toEpochMilli()
            } catch (e: Exception) {
                continue
            }
        }

        Log.e(TAG, "Failed to parse date: '$dateString'. Please add a new pattern to DateParser.")
        return null
    }
}