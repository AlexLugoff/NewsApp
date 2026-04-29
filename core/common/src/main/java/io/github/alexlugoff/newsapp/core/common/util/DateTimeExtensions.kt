package io.github.alexlugoff.newsapp.core.common.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val PATTERN_DATE_AND_TIME = "dd.MM.yyyy HH:mm"

private fun getDateTimeFormatter() =
    DateTimeFormatter.ofPattern(PATTERN_DATE_AND_TIME, Locale.getDefault())

private fun Long.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(
        Instant.ofEpochMilli(this),
        ZoneId.systemDefault()
    )
}

val Long.asDateTimeString: String
    get() = this.toLocalDateTime().format(getDateTimeFormatter())
