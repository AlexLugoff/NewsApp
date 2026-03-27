package com.example.newsapp.extensions

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.text.style.StyleSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.example.newsapp.R
import com.example.newsapp.core.common.UiText
import com.example.newsapp.data.exception.DataError

fun Context.showShortToast(text: String): Toast {
    return Toast.makeText(this, text, Toast.LENGTH_SHORT).also { it.show() }
}

fun Context.showShortToast(@StringRes resId: Int, vararg args: Any): Toast {
    return showShortToast(getString(resId, *args))
}

fun Context.showLongToast(text: String): Toast {
    return Toast.makeText(this, text, Toast.LENGTH_LONG).also { it.show() }
}

fun Context.showLongToast(@StringRes resId: Int, vararg args: Any): Toast {
    return showLongToast(getString(resId, *args))
}

var View.isAvailable: Boolean
    get() = isEnabled && isClickable
    set(value) {
        isEnabled = value
        isClickable = value
        alpha = if (value) 1.0F else 0.4F
    }

fun String?.toSpannedHtml(): Spanned {
    val source = this.orEmpty()

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(source, Html.FROM_HTML_MODE_COMPACT)
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(source)
    }
}

fun DataError.toReadableText(): UiText {
    return when (this) {
        is DataError.Network.HttpError -> UiText.StringResource(
            R.string.error_http_code,
            this.code,
            this.message
        )

        is DataError.Network.UnknownHost -> message?.let {
            UiText.StringResource(
                R.string.error_message_unknown_host,
                this.message
            )
        } ?: UiText.StringResource(R.string.error_unknown_host)

        is DataError.Parser.InvalidFormat -> UiText.StringResource(
            R.string.error_parser,
            this.message
        )

        is DataError.Network.Unknown -> message?.let {
            UiText.StringResource(
                R.string.error_unknown_message,
                this.message
            )
        } ?: UiText.StringResource(R.string.error_unknown)

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
