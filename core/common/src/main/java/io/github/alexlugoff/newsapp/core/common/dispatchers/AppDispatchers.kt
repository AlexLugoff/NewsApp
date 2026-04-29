package io.github.alexlugoff.newsapp.core.common.dispatchers

import kotlinx.coroutines.CoroutineDispatcher

data class AppDispatchers(
    val main: CoroutineDispatcher,
    val default: CoroutineDispatcher,
    val io: CoroutineDispatcher,
    val unconfined: CoroutineDispatcher
)