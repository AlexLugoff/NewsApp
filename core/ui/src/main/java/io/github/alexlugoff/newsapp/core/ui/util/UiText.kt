package io.github.alexlugoff.newsapp.core.ui.util

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource

sealed class UiText {

    companion object {
        private const val EMPTY = ""
    }

    data class Dynamic(val value: String) : UiText()
    class StringResource(@param:StringRes val id: Int, vararg val args: Any) : UiText()
    data class PluralResource(@param:PluralsRes val id: Int, val quantity: Int) : UiText()
    object Empty : UiText()

    fun asString(context: Context): String {
        return when (this) {
            is Dynamic -> value
            is StringResource -> context.getString(id, *args)
            is PluralResource -> context.resources.getQuantityString(id, quantity, quantity)
            Empty -> EMPTY
        }
    }

    @Composable
    fun asString(): String {
        return when (this) {
            is Dynamic -> value
            is StringResource -> stringResource(id, *args)
            is PluralResource -> pluralStringResource(id, quantity, quantity)
            Empty -> EMPTY
        }
    }
}
