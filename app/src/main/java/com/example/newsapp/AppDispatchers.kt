package com.example.newsapp

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher

open class AppDispatchers(
    val main: MainCoroutineDispatcher,
    val default: CoroutineDispatcher,
    val io: CoroutineDispatcher,
    val unconfined: CoroutineDispatcher
)