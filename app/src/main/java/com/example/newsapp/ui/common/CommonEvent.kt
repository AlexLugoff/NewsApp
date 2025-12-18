package com.example.newsapp.ui.common

import androidx.annotation.StringRes

sealed class CommonEvent {

    class ShowShortToast(
        val text: String? = null,
        @StringRes val textResId: Int? = null,
        val args: Array<Any> = emptyArray()
    ) : CommonEvent()

    class ShowLongToast(
        val text: String? = null,
        @StringRes val textResId: Int? = null,
        val args: Array<Any> = emptyArray()
    ) : CommonEvent()

}