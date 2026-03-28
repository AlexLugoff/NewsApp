package com.example.newsapp.core.ui.modifier

import android.os.SystemClock
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role

/**
 * Модификатор для защиты от множественных быстрых нажатий (Throttle).
 */
fun Modifier.clickableSingle(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    delay: Long = 500L,
    interactionSource: MutableInteractionSource? = null,
    indication: Indication? = null,
    onClick: () -> Unit
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "clickableSingle"
        properties["enabled"] = enabled
        properties["delay"] = delay
        properties["role"] = role
    }
) {
    val handler = rememberClickHandler(delay)
    val onClickState = rememberUpdatedState(onClick)

    val localSource = interactionSource ?: remember { MutableInteractionSource() }
    val localIndication = indication ?: LocalIndication.current

    this.clickable(
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
        interactionSource = localSource,
        indication = localIndication
    ) {
        handler.processEvent { onClickState.value() }
    }
}

@Composable
private fun rememberClickHandler(delay: Long): ClickHandler {
    return remember(delay) { ClickHandler(delay) }
}

private class ClickHandler(private val delay: Long) {
    private var lastClickTime = 0L

    fun processEvent(event: () -> Unit) {
        val currentTime = SystemClock.uptimeMillis()
        if (currentTime - lastClickTime > delay) {
            lastClickTime = currentTime
            event()
        }
    }
}
