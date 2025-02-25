package com.jefisu.authenticator.core.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import authenticator.composeapp.generated.resources.Res
import authenticator.composeapp.generated.resources.please_wait
import com.composables.core.ModalSheetProperties
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.jetbrains.compose.resources.stringResource

object LoadingOverlayController {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun showLoading() {
        _isLoading.value = true
    }

    fun hideLoading() {
        _isLoading.value = false
    }
}

@Composable
fun LoadingOverlay() {
    val sheetState = rememberModalBottomSheetState(SheetDetent.Hidden)

    LaunchedEffect(Unit) {
        LoadingOverlayController.isLoading.collect { isLoading ->
            sheetState.currentDetent =
                if (isLoading) SheetDetent.FullyExpanded else SheetDetent.Hidden
        }
    }

    BottomSheet(
        sheetState = sheetState,
        properties = ModalSheetProperties(
            dismissOnClickOutside = false
        ),
        sheetContent = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(Res.string.please_wait),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(16.dp))
                CircularProgressIndicator()
            }
        }
    ) {
    }
}
