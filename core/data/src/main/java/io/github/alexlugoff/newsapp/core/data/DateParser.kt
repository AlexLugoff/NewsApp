package io.github.alexlugoff.newsapp.core.data

import timber.log.Timber
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateParser {
    private val formatters = listOf(
        DateTimeFormatter.RFC_1123_DATE_TIME,
        DateTimeFormatter.ISO_DATE_TIME,
        DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.US),
        DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss Z", Locale.US),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US)
    )

    fun parseToLong(dateString: String?): Long? {
        if (dateString.isNullOrBlank()) {
            Timber.w("Empty or null date string received")
            return null
        }

        val cleanedDate = dateString.trim()

        for (formatter in formatters) {
            try {
                return ZonedDateTime.parse(cleanedDate, formatter)
                    .toInstant()
                    .toEpochMilli()
            } catch (e: Exception) {
                // Timber.e(e.stackTraceToString())
                continue
            }
        }

        Timber.e("Failed to parse date: '$dateString'. Please add a new pattern to DateParser.")
        return null
    }
}
