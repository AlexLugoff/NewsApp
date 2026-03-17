package com.example.newsapp.presentation.common

import androidx.annotation.StringRes
import com.example.newsapp.presentation.UiText

sealed class CommonEvent {

    class ShowShortToast(
        val text: String? = null,
        @param:StringRes val textResId: Int? = null,
        val args: Array<Any> = emptyArray(),
        val uiText: UiText? = null
    ) : CommonEvent()

    class ShowLongToast(
        val text: String? = null,
        @param:StringRes val textResId: Int? = null,
        val args: Array<Any> = emptyArray(),
        val uiText: UiText? = null
    ) : CommonEvent()

}