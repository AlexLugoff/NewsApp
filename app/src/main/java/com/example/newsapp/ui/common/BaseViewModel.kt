package com.example.newsapp.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber

abstract class BaseViewModel<ViewState, Event> : ViewModel() {

    protected companion object {
        const val DELAY_BEFORE_NAVIGATING_BACK = 1500L
        const val DELAY_BEFORE_HIDING_PROGRESS_DIALOG = 500L
    }

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    private val _event = SingleLiveEvent<Event>()
    val event: LiveData<Event> = _event

    private val _commonEvent = SingleLiveEvent<CommonEvent>()
    val commonEvent: LiveData<CommonEvent> = _commonEvent

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

    protected fun CommonEvent.postValue() {
        _commonEvent.postValue(this)
    }

    protected fun CommonEvent.setValue() {
        _commonEvent.value = this
    }

    protected fun logError(t: Throwable) {
        Timber.tag(this::class.java.simpleName).e(t.stackTraceToString())
    }
}