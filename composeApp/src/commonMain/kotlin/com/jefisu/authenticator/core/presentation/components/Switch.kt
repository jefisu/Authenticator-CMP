package com.jefisu.authenticator.core.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jefisu.authenticator.core.presentation.theme.LightOptCodeColor

@Composable
fun Switch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    shape: CornerBasedShape = CircleShape,
    gapBetweenThumbAndTrackEdge: Dp = 2.dp,
    checkedTrackColor: Color = LightOptCodeColor,
    uncheckedTrackColor: Color = checkedTrackColor.copy(alpha = .3f),
    switchWidth: Dp = 44.dp,
    switchHeight: Dp = 24.dp
) {
    val density = LocalDensity.current

    val thumbRadius = (switchHeight / 2) - gapBetweenThumbAndTrackEdge
    val maxOffset = switchWidth - thumbRadius - gapBetweenThumbAndTrackEdge
    val minOffset = thumbRadius + gapBetweenThumbAndTrackEdge

    val targetValue = remember(checked) {
        with(density) { if (checked) maxOffset.toPx() else minOffset.toPx() }
    }
    val offsetXAnim = remember { Animatable(targetValue) }

    val colorAnim by animateColorAsState(
        targetValue = if (checked) checkedTrackColor else uncheckedTrackColor,
        animationSpec = tween(durationMillis = 300)
    )

    LaunchedEffect(targetValue) {
        if (offsetXAnim.targetValue != targetValue) {
            offsetXAnim.animateTo(targetValue, tween(durationMillis = 300))
        }
    }

    Canvas(
        modifier = modifier
            .width(switchWidth)
            .height(switchHeight)
            .clip(shape)
            .toggleable(
                value = checked,
                onValueChange = onCheckedChange
            )
    ) {
        val radius = shape.topEnd.toPx(size, this)

        drawRoundRect(
            color = colorAnim,
            cornerRadius = CornerRadius(radius)
        )
        drawCircle(
            color = Color.White,
            radius = thumbRadius.toPx(),
            center = center.copy(
                x = offsetXAnim.value
            )
        )
    }
}
