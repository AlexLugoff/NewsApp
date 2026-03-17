package com.example.newsapp.presentation.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException

abstract class BaseViewModel<ViewState, Event> : ViewModel() {

    private val _uiStateFlow = MutableStateFlow<ViewState?>(null)
    open val uiStateFlow: StateFlow<ViewState?> = _uiStateFlow.asStateFlow()

    // Разовые события экрана (Navigation, UI logic)
    private val _eventFlow = MutableSharedFlow<Event>(extraBufferCapacity = 1)
    val eventFlow = _eventFlow.asSharedFlow()

    // Общие события (Toast, Dialogs)
    private val _commonEventFlow = MutableSharedFlow<CommonEvent>(extraBufferCapacity = 1)
    val commonEventFlow = _commonEventFlow.asSharedFlow()

    protected val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        if (throwable is CancellationException) return@CoroutineExceptionHandler
        handleError(throwable)
    }

    protected fun Event.send() {
        _eventFlow.tryEmit(this)
    }

    protected fun CommonEvent.send() {
        _commonEventFlow.tryEmit(this)
    }

    protected fun ViewState.update() {
        _uiStateFlow.value = this
    }

    protected fun logError(t: Throwable) {
        Timber.tag(this::class.java.simpleName).e(t.stackTraceToString())
    }

    private fun handleError(error: Throwable) {
        Timber.e("Произошла ошибка в корутине :${error.stackTraceToString()}")
    }
}