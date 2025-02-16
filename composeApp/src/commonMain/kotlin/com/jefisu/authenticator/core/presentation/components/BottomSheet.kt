package com.jefisu.authenticator.core.presentation.components

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.composables.core.ModalBottomSheet
import com.composables.core.ModalBottomSheetState
import com.composables.core.ModalSheetProperties
import com.composables.core.Scrim
import com.composables.core.Sheet
import com.jefisu.authenticator.core.presentation.theme.colors

@Composable
fun BottomSheet(
    sheetState: ModalBottomSheetState,
    modifier: Modifier = Modifier,
    properties: ModalSheetProperties = ModalSheetProperties(),
    sheetContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    content()

    ModalBottomSheet(state = sheetState, properties = properties) {
        Scrim(
            enter = fadeIn(),
            exit = fadeOut()
        )
        Sheet(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp, 16.dp))
                .background(MaterialTheme.colors.backgroundColor)
                .navigationBarsPadding()
                .imePadding()
        ) {
            sheetContent()
        }
    }
}
