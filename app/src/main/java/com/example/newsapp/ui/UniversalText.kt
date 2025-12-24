package com.example.newsapp.ui

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

sealed class UniversalText {

    companion object {
        private const val EMPTY = ""
    }

    data class Dynamic(val value: String) : UniversalText()
    class Resource(@param:StringRes val id: Int, vararg val args: Any) : UniversalText()
    data class PluralResource(@param:PluralsRes val id: Int, val quantity: Int) : UniversalText()
    object Empty : UniversalText()

    fun asString(context: Context): String {
        return when (this) {
            is Dynamic -> value
            is Resource -> context.getString(id, *args)
            is PluralResource -> context.resources.getQuantityString(id, quantity, quantity)
            Empty -> EMPTY
        }
    }
}