package com.example.newsapp.presentation.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException

abstract class BaseViewModel<ViewState, Event> : ViewModel() {

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    private val _uiStateFlow = MutableStateFlow<ViewState?>(null)
    open val uiStateFlow: StateFlow<ViewState?> = _uiStateFlow.asStateFlow()

    private val _event = SingleLiveEvent<Event>()
    val event: LiveData<Event> = _event

    private val _commonEvent = SingleLiveEvent<CommonEvent>()
    val commonEvent: LiveData<CommonEvent> = _commonEvent

    protected val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        if (throwable is CancellationException) return@CoroutineExceptionHandler
        handleError(throwable)
    }

    @JvmName("postEventValue")
    protected fun Event.postValue() {
        _event.postValue(this)
    }

    @JvmName("setEventValue")
    protected fun Event.setValue() {
        _event.value = this
    }

    @JvmName("postViewStateValue")
    protected fun ViewState.postValue() {
        _viewState.postValue(this)
    }

    @JvmName("setViewStateValue")
    protected fun ViewState.setValue() {
        _viewState.value = this
    }

    protected fun ViewState.update() {
        _uiStateFlow.value = this
    }

    protected fun CommonEvent.postValue() {
        _commonEvent.postValue(this)
    }

    protected fun CommonEvent.setValue() {
        _commonEvent.value = this
    }

    protected fun logError(t: Throwable) {
        Timber.tag(this::class.java.simpleName).e(t.stackTraceToString())
    }

    private fun handleError(error: Throwable) {
        Timber.e("Произошла ошибка в корутине :${error.stackTraceToString()}")
    }
}