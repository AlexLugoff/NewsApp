package com.example.newsapp

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.newsapp.ui.SafeOnClickListener
import kotlin.coroutines.cancellation.CancellationException

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

fun Fragment.showShortToast(@StringRes resId: Int, vararg args: Any) = requireContext().showShortToast(resId, *args)

fun Fragment.showLongToast(text: String) = requireContext().showLongToast(text)

fun Fragment.showLongToast(@StringRes resId: Int, vararg args: Any) = requireContext().showLongToast(resId, *args)

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

fun View.hide() {
    this.isVisible = false
}

fun View.show() {
    this.isVisible = true
}

var View.isAvailable: Boolean
    get() = isEnabled && isClickable
    set(value) {
        isEnabled = value
        isClickable = value
        alpha = if (value) 1.0F else 0.4F
    }

/**
 * Like [runCatching], but with proper coroutines cancellation handling. Also only catches [Exception] instead of [Throwable].
 *
 * Cancellation exceptions need to be rethrown. See https://github.com/Kotlin/kotlinx.coroutines/issues/1814.
 */
inline fun <R> resultOf(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}

/**
 * Like [runCatching], but with proper coroutines cancellation handling. Also only catches [Exception] instead of [Throwable].
 *
 * Cancellation exceptions need to be rethrown. See https://github.com/Kotlin/kotlinx.coroutines/issues/1814.
 */
inline fun <T, R> T.resultOf(block: T.() -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}

sealed interface SealedResult<out T, out E> {
    data class Success<out T>(val data: T) : SealedResult<T, Nothing>
    data class Failure<out E>(val error: E) : SealedResult<Nothing, E>
}

inline fun <T, E, R> SealedResult<T, E>.fold(
    onSuccess: (T) -> R,
    onFailure: (E) -> R
): R {
    return when (this) {
        is SealedResult.Success -> onSuccess(data)
        is SealedResult.Failure -> onFailure(error)
    }
}
