package com.jefisu.authenticator.presentation.totp.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

enum class SwipeDirection {
    START_TO_END,
    END_TO_START,
    BOTH
}

@Composable
fun SwipeableItemWithActions(
    isRevealed: Boolean,
    startActions: (@Composable RowScope.() -> Unit)? = null,
    endActions: (@Composable RowScope.() -> Unit)? = null,
    modifier: Modifier = Modifier,
    onExpanded: () -> Unit = {},
    onCollapsed: () -> Unit = {},
    content: @Composable () -> Unit
) {
    var startActionsWidth by remember { mutableFloatStateOf(0f) }
    var endActionsWidth by remember { mutableFloatStateOf(0f) }
    val offset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    val direction = remember(startActions, endActions) {
        when {
            startActions != null && endActions != null -> SwipeDirection.BOTH
            startActions != null -> SwipeDirection.START_TO_END
            endActions != null -> SwipeDirection.END_TO_START
            else -> SwipeDirection.END_TO_START
        }
    }

    val maxStartOffset =
        if (direction == SwipeDirection.START_TO_END || direction == SwipeDirection.BOTH) {
            startActionsWidth
        } else {
            0f
        }
    val maxEndOffset =
        if (direction == SwipeDirection.END_TO_START || direction == SwipeDirection.BOTH) {
            endActionsWidth
        } else {
            0f
        }

    LaunchedEffect(isRevealed) {
        if (!isRevealed) {
            offset.animateTo(0f)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        if (startActions != null) {
            ActionsRow(
                actions = startActions,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .onSizeChanged { startActionsWidth = it.width.toFloat() }
            )
        }

        if (endActions != null) {
            ActionsRow(
                actions = endActions,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .onSizeChanged { endActionsWidth = it.width.toFloat() }
            )
        }

        SwipeableSurface(
            offset = offset.value,
            onDrag = { dragAmount ->
                scope.launch {
                    when (direction) {
                        SwipeDirection.START_TO_END -> {
                            val newOffset = (offset.value + dragAmount).coerceIn(0f, maxStartOffset)
                            offset.snapTo(newOffset)
                        }

                        SwipeDirection.END_TO_START -> {
                            val newOffset = (offset.value + dragAmount).coerceIn(-maxEndOffset, 0f)
                            offset.snapTo(newOffset)
                        }

                        SwipeDirection.BOTH -> {
                            val newOffset =
                                (offset.value + dragAmount).coerceIn(-maxEndOffset, maxStartOffset)
                            offset.snapTo(newOffset)
                        }
                    }
                }
            },
            onDragEnd = {
                scope.launch {
                    when {
                        offset.value > 0 && offset.value >= maxStartOffset / 2f -> {
                            offset.animateTo(maxStartOffset)
                            onExpanded()
                        }

                        offset.value < 0 && offset.value <= -maxEndOffset / 2f -> {
                            offset.animateTo(-maxEndOffset)
                            onExpanded()
                        }

                        else -> {
                            offset.animateTo(0f)
                            onCollapsed()
                        }
                    }
                }
            },
            content = content
        )
    }
}

@Composable
private fun ActionsRow(
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier.fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically,
        content = actions
    )
}

@Composable
private fun SwipeableSurface(
    offset: Float,
    onDrag: (dragAmount: Float) -> Unit,
    onDragEnd: () -> Unit,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .offset { IntOffset(offset.roundToInt(), 0) }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { _, dragAmount -> onDrag(dragAmount) },
                    onDragEnd = { onDragEnd() }
                )
            }
    ) {
        content()
    }
}
