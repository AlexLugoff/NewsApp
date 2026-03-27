package com.example.newsapp.core.common

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val PATTERN_DATE = "dd MMM yyyy"
private const val PATTERN_DATE_AND_TIME = "dd.MM.yyyy HH:mm"

private val dateFormatter = DateTimeFormatter.ofPattern(PATTERN_DATE, Locale.getDefault())
private val dateAndTimeFormatter =
    DateTimeFormatter.ofPattern(PATTERN_DATE_AND_TIME, Locale.getDefault())

private fun Long.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(
        Instant.ofEpochMilli(this),
        ZoneId.systemDefault()
    )
}

val Long.asDateString: String
    get() = this.toLocalDateTime().format(dateFormatter)

val Long.asDateTimeString: String
    get() = this.toLocalDateTime().format(dateAndTimeFormatter)