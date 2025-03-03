package com.jefisu.authenticator.presentation.totp.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateOffset
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.jefisu.authenticator.core.presentation.theme.colors
import com.jefisu.authenticator.presentation.util.TestTag
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@ExperimentalAnimationApi
@Composable
fun ArcExpandingFAB(
    modifier: Modifier = Modifier,
    items: List<FABItem>,
    isExpanded: Boolean,
    fabColor: Color = MaterialTheme.colors.cardColor,
    fabShape: Shape = CircleShape,
    onExpandToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    require(items.size <= 3) {
        "Maximum 3 items can be specified"
    }

    val transition = updateTransition(targetState = isExpanded)

    Box(modifier = modifier) {
        val positions = calculateCircularPositions(
            numElements = items.size,
            radius = with(LocalDensity.current) { 90.dp.toPx() },
            startAngle = 180f,
            sweepAngle = 90f
        )

        items.reversed().forEachIndexed { index, fabItem ->
            val durationMillis = 300
            val delayMillis = index * 100
            val easing = FastOutSlowInEasing
            val offset by transition.animateOffset(
                transitionSpec = {
                    tween(
                        durationMillis = durationMillis,
                        delayMillis = delayMillis,
                        easing = easing
                    )
                }
            ) { isExpanded ->
                if (isExpanded) {
                    Offset(positions[index].x, positions[index].y)
                } else {
                    Offset.Zero
                }
            }

            if (offset != Offset.Zero) {
                FloatingActionButton(
                    onClick = {
                        fabItem.onClick()
                        onExpandToggle()
                    },
                    containerColor = MaterialTheme.colors.cardColor,
                    contentColor = MaterialTheme.colors.textColor,
                    shape = CircleShape,
                    modifier = Modifier
                        .offset { IntOffset(offset.x.toInt(), offset.y.toInt()) }
                        .then(fabItem.modifier)
                ) {
                    Icon(
                        painter = painterResource(fabItem.iconRes),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = onExpandToggle,
            containerColor = fabColor,
            shape = fabShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .testTag(TestTag.ARC_EXPANDING_FAB)
        ) {
            content()
        }
    }
}

private fun calculateCircularPositions(
    numElements: Int,
    radius: Float,
    startAngle: Float = 0f,
    sweepAngle: Float = 360f,
    includeEnds: Boolean = true
): List<Offset> {
    val angleIncrement = if (includeEnds) {
        sweepAngle / (numElements - 1)
    } else {
        sweepAngle / numElements
    }

    return List(numElements) { index ->
        val angleDegrees = startAngle + index * angleIncrement
        val angleRadians = degreesToRadians(angleDegrees)
        Offset(
            x = radius * cos(angleRadians).toFloat(),
            y = radius * sin(angleRadians).toFloat()
        )
    }
}

private fun degreesToRadians(degrees: Float): Double {
    return degrees * (PI / 180f)
}

data class FABItem(
    val iconRes: DrawableResource,
    val onClick: () -> Unit,
    val modifier: Modifier = Modifier
)
