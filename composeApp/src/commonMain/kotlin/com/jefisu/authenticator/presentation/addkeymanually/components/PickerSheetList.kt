package com.jefisu.authenticator.presentation.addkeymanually.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import authenticator.composeapp.generated.resources.Res
import authenticator.composeapp.generated.resources.cancel
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
import com.jefisu.authenticator.core.presentation.components.BottomSheet
import com.jefisu.authenticator.core.presentation.theme.colors
import org.jetbrains.compose.resources.stringResource

@Composable
fun PickerSheetList(
    title: String,
    items: List<String>,
    selectedItem: String,
    onSelectItem: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(SheetDetent.Hidden)
    val iconRotation by animateFloatAsState(
        targetValue = if (!sheetState.isIdle) 180f else 0f,
        animationSpec = tween()
    )

    BottomSheet(
        sheetState = sheetState,
        sheetContent = {
            PickerSheetContent(
                items = items,
                onSelectItem = { item ->
                    onSelectItem(item)
                    sheetState.currentDetent = SheetDetent.Hidden
                },
                onCancel = { sheetState.currentDetent = SheetDetent.Hidden }
            )
        }
    ) {
        PickerTrigger(
            title = title,
            selectedItem = selectedItem,
            iconRotation = iconRotation,
            onClick = { sheetState.currentDetent = SheetDetent.FullyExpanded },
            modifier = modifier
        )
    }
}

@Composable
private fun PickerSheetContent(
    items: List<String>,
    onSelectItem: (String) -> Unit,
    onCancel: () -> Unit
) {
    Column {
        items.forEach { item ->
            PickerItem(
                text = item,
                onClick = { onSelectItem(item) }
            )
            if (item != items.last()) {
                HorizontalDivider(
                    color = MaterialTheme.colors.textColor.copy(alpha = .1f)
                )
            }
        }
        HorizontalDivider(
            color = MaterialTheme.colors.textColor.copy(alpha = .1f),
            thickness = 8.dp
        )
        PickerItem(
            text = stringResource(Res.string.cancel),
            onClick = onCancel
        )
    }
}

@Composable
private fun PickerItem(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        contentColor = MaterialTheme.colors.textColor
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )
    }
}

@Composable
private fun PickerTrigger(
    title: String,
    selectedItem: String,
    iconRotation: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Text(
            text = title,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = MaterialTheme.colors.optCodeColor,
            modifier = Modifier.padding(start = 2.dp)
        )
        Surface(
            color = MaterialTheme.colors.backgroundColor,
            contentColor = MaterialTheme.colors.textColor,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colors.textColor.copy(alpha = .3f)
            ),
            onClick = onClick,
            modifier = modifier
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = selectedItem,
                    style = MaterialTheme.typography.bodyLarge
                )
                Icon(
                    imageVector = Icons.Rounded.ArrowDropDown,
                    contentDescription = "Show options",
                    modifier = Modifier
                        .graphicsLayer { rotationZ = iconRotation }
                        .scale(1.5f)
                )
            }
        }
    }
}
