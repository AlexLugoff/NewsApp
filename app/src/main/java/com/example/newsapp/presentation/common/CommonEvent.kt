package com.example.newsapp.presentation.common

import androidx.annotation.StringRes

sealed class CommonEvent {

    class ShowShortToast(
        val text: String? = null,
        @param:StringRes val textResId: Int? = null,
        val args: Array<Any> = emptyArray()
    ) : CommonEvent()

    class ShowLongToast(
        val text: String? = null,
        @param:StringRes val textResId: Int? = null,
        val args: Array<Any> = emptyArray()
    ) : CommonEvent()

}