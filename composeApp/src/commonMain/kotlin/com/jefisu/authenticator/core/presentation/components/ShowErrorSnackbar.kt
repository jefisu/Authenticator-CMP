package com.jefisu.authenticator.core.presentation.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun ShowErrorSnackbar(
    error: String?,
    snackbarHostState: SnackbarHostState,
    onDismiss: () -> Unit
) {
    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(
                message = it,
                withDismissAction = true
            )
            onDismiss()
        }
    }
}
