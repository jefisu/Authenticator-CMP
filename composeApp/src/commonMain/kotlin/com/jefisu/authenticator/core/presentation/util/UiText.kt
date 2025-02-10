package com.jefisu.authenticator.core.presentation.util

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

typealias TextResource = StringResource

sealed interface UiText {
    data class DynamicString(val value: String) : UiText
    data class StringResource(
        val resId: TextResource,
        val args: Array<Any> = emptyArray()
    ) : UiText

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(resId, *args)
        }
    }
}
