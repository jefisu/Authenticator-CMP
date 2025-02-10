@file:OptIn(ExperimentalPermissionsApi::class)

package com.jefisu.authenticator.core.presentation.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.mohamedrejeb.calf.permissions.ExperimentalPermissionsApi
import com.mohamedrejeb.calf.permissions.PermissionState
import com.mohamedrejeb.calf.permissions.PermissionStatus
import com.mohamedrejeb.calf.permissions.isNotGranted

@Composable
fun PermissionHandler(
    permissionState: PermissionState,
    onPermissionGranted: @Composable () -> Unit,
    onPermissionDenied: @Composable () -> Unit
) {
    if (permissionState.status.isNotGranted) {
        LaunchedEffect(Unit) {
            permissionState.launchPermissionRequest()
        }
    }

    when (permissionState.status) {
        PermissionStatus.Granted -> onPermissionGranted()
        else -> onPermissionDenied()
    }
}
