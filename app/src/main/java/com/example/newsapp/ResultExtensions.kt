package com.example.newsapp

import kotlin.coroutines.cancellation.CancellationException

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

inline fun <T, E> SealedResult<T, E>.onSuccess(
    action: (T) -> Unit
): SealedResult<T, E> {
    if (this is SealedResult.Success) {
        action(data)
    }
    return this
}

inline fun <T, E> SealedResult<T, E>.onFailure(
    action: (E) -> Unit
): SealedResult<T, E> {
    if (this is SealedResult.Failure) {
        action(error)
    }
    return this
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