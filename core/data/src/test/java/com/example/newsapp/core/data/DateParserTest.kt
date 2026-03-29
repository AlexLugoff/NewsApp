package com.example.newsapp.core.data

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import org.junit.Test

class DateParserTest {

    @Test
    fun parseToLong_allFormats_areSupported() {
        val formats = listOf(
            "Tue, 15 Jul 2025 10:00:00 GMT" to 1752573600000L,
            "2025-07-15T10:00:00Z" to 1752573600000L,
            "2025-07-15 10:00:00" to 1752573600000L
        )

        formats.forEach { (input, expected) ->
            val result = DateParser.parseToLong(input)
            assertEquals("Failed to parse: $input", expected, result)
        }
    }

    @Test
    fun `parseToLong - when valid RFC_1123 date - returns correct timestamp`() {
        val input = "Tue, 15 Jul 2025 10:00:00 GMT"
        val result = DateParser.parseToLong(input)
        assertEquals(1752573600000L, result)
    }

    @Test
    fun `parseToLong - when ISO_8601 date - returns correct timestamp`() {
        val input = "2025-07-15T10:00:00Z"
        val result = DateParser.parseToLong(input)
        assertNotNull(result)
    }

    @Test
    fun `parseToLong - when invalid string - returns null and logs error`() {
        val input = "Not a date"
        val result = DateParser.parseToLong(input)
        assertNull(result)
    }

    @Test
    fun `parseToLong - when null or empty - returns null`() {
        assertNull(DateParser.parseToLong(null))
        assertNull(DateParser.parseToLong("   "))
    }
}
