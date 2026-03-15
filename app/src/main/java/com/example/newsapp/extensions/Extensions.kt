package com.example.newsapp.extensions

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.View
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.newsapp.R
import com.example.newsapp.presentation.SafeOnClickListener

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

fun Activity.getRootFragment(): Fragment? {
    return (this as? AppCompatActivity)
        ?.supportFragmentManager
        ?.findFragmentById(R.id.nav_host_fragment)
        ?.childFragmentManager
        ?.fragments
        ?.first()
}

fun Fragment.showShortToast(text: String) = requireContext().showShortToast(text)

fun Fragment.showShortToast(@StringRes resId: Int, vararg args: Any) =
    requireContext().showShortToast(resId, *args)

fun Fragment.showLongToast(text: String) = requireContext().showLongToast(text)

fun Fragment.showLongToast(@StringRes resId: Int, vararg args: Any) =
    requireContext().showLongToast(resId, *args)

fun View.setSafeOnClickListener(onClick: (View) -> Unit) {
    setOnClickListener(SafeOnClickListener { onClick(it) })
}

fun Fragment.navigate(@IdRes resId: Int) {
    findNavController().navigate(resId)
}

fun Fragment.navigate(@IdRes resId: Int, args: Bundle) {
    findNavController().navigate(resId, args)
}

fun Fragment.navigate(navDirections: NavDirections) {
    findNavController().navigate(navDirections)
}

fun Fragment.navigate(navDirections: NavDirections, navOptions: NavOptions) {
    findNavController().navigate(navDirections, navOptions)
}

var View.isAvailable: Boolean
    get() = isEnabled && isClickable
    set(value) {
        isEnabled = value
        isClickable = value
        alpha = if (value) 1.0F else 0.4F
    }

fun String?.toSpannedHtml(): Spanned {
    if (this == null) return Html.fromHtml("", Html.FROM_HTML_MODE_LEGACY)

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(this)
    }
}
