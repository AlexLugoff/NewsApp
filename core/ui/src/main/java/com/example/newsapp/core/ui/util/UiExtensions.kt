package com.example.newsapp.core.ui.util

import android.graphics.Typeface
import android.text.style.StyleSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.example.newsapp.core.common.error.DataError
import com.example.newsapp.core.common.R
import com.example.newsapp.core.common.util.toSpannedHtml

fun DataError.toReadableText(): UiText {
    return when (this) {
        is DataError.Network.HttpError -> UiText.StringResource(
            R.string.error_http_code,
            this.code,
            this.message
        )

        is DataError.Network.UnknownHost -> {
            val msg = this.message
            if (msg != null) {
                UiText.StringResource(
                    R.string.error_message_unknown_host,
                    msg
                )
            } else {
                UiText.StringResource(R.string.error_unknown_host)
            }
        }

        is DataError.Parser.InvalidFormat -> UiText.StringResource(
            R.string.error_parser,
            this.message
        )

        is DataError.Network.Unknown -> {
            val msg = this.message
            if (msg != null) {
                UiText.StringResource(
                    R.string.error_unknown_message,
                    msg
                )
            } else {
                UiText.StringResource(R.string.error_unknown)
            }
        }

        is DataError.Network.ConnectionTimeout -> UiText.StringResource(
            R.string.error_message_connection_timeout
        )

        is DataError.Local.NotFound -> UiText.StringResource(R.string.error_local_not_found)
        else -> UiText.StringResource(R.string.error_unknown)
    }
}

fun String.toAnnotatedString(): AnnotatedString {
    val spanned = this.toSpannedHtml()
    return buildAnnotatedString {
        append(spanned.toString())

        val spans = spanned.getSpans(0, spanned.length, Any::class.java)

        spans.forEach { span ->
            val start = spanned.getSpanStart(span)
            val end = spanned.getSpanEnd(span)

            when (span) {
                is StyleSpan -> {
                    val fontWeight =
                        if (span.style == Typeface.BOLD || span.style == Typeface.BOLD_ITALIC)
                            FontWeight.Bold else FontWeight.Normal
                    val fontStyle =
                        if (span.style == Typeface.ITALIC || span.style == Typeface.BOLD_ITALIC)
                            FontStyle.Italic else FontStyle.Normal

                    addStyle(SpanStyle(fontWeight = fontWeight, fontStyle = fontStyle), start, end)
                }

                is UnderlineSpan -> {
                    addStyle(SpanStyle(textDecoration = TextDecoration.Underline), start, end)
                }

                is URLSpan -> {
                    val link = LinkAnnotation.Url(
                        url = span.url,
                        styles = TextLinkStyles(
                            style = SpanStyle(
                                color = Color(0xFF1A73E8),
                                textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    )
                    addLink(link, start, end)
                }
            }
        }
    }
}