package io.github.alexlugoff.newsapp.core.common.util

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes

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
