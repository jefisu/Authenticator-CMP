@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalPermissionsApi::class,
    ExperimentalSharedTransitionApi::class
)

package com.jefisu.authenticator.presentation.qrscanner

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import authenticator.composeapp.generated.resources.Res
import authenticator.composeapp.generated.resources.cant_scan_qr_code
import authenticator.composeapp.generated.resources.enable_camera
import authenticator.composeapp.generated.resources.enable_camera_permission
import authenticator.composeapp.generated.resources.enter_key_manually
import authenticator.composeapp.generated.resources.ic_camera_permission
import authenticator.composeapp.generated.resources.ic_flash_off
import authenticator.composeapp.generated.resources.ic_flash_on
import authenticator.composeapp.generated.resources.ic_gallery
import authenticator.composeapp.generated.resources.invalid_qr_code
import authenticator.composeapp.generated.resources.point_camera_at_qr_code
import authenticator.composeapp.generated.resources.scan_qr_code
import com.jefisu.authenticator.core.presentation.components.LoadingOverlayController
import com.jefisu.authenticator.core.presentation.components.ShowErrorSnackbar
import com.jefisu.authenticator.core.presentation.permission.PermissionDeniedScreen
import com.jefisu.authenticator.core.presentation.permission.PermissionHandler
import com.jefisu.authenticator.core.presentation.sharedtransition.SharedTransitionKeys.FAB_EXPLODE_BOUNDS_KEY
import com.jefisu.authenticator.core.presentation.sharedtransition.sharedTransition
import com.jefisu.authenticator.core.presentation.theme.LightOptCodeFinishingColor
import com.jefisu.authenticator.core.presentation.theme.colors
import com.mohamedrejeb.calf.core.LocalPlatformContext
import com.mohamedrejeb.calf.io.readByteArray
import com.mohamedrejeb.calf.permissions.ExperimentalPermissionsApi
import com.mohamedrejeb.calf.permissions.Permission
import com.mohamedrejeb.calf.permissions.rememberPermissionState
import com.mohamedrejeb.calf.picker.FilePickerFileType
import com.mohamedrejeb.calf.picker.rememberFilePickerLauncher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import qrscanner.CameraLens
import qrscanner.QrScanner

@Composable
fun QrScannerScreen(
    onNavigateBack: () -> Unit,
    onNavigateToEnterKeyManually: () -> Unit
) {
    val viewModel = koinViewModel<QrScannerViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val cameraPermission = rememberPermissionState(Permission.Camera)

    LaunchedEffect(Unit) {
        viewModel.state.collect { state ->
            if (state.createdAccount) onNavigateBack()
        }
    }

    PermissionHandler(
        permissionState = cameraPermission,
        onPermissionGranted = {
            QrScannerScreenContent(
                state = state,
                onAction = { action ->
                    when (action) {
                        QrScannerAction.NavigateBack -> onNavigateBack()
                        QrScannerAction.EnterKeyManually -> onNavigateToEnterKeyManually()
                        else -> viewModel.onAction(action)
                    }
                }
            )
        },
        onPermissionDenied = {
            PermissionDeniedScreen(
                title = stringResource(Res.string.enable_camera),
                description = stringResource(Res.string.enable_camera_permission),
                iconRes = Res.drawable.ic_camera_permission,
                onClickOpenSettings = cameraPermission::openAppSettings
            )
        }
    )
}

@Composable
fun QrScannerScreenContent(
    state: QrScannerState,
    onAction: (QrScannerAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val errorMessage = stringResource(Res.string.invalid_qr_code)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val platformContext = LocalPlatformContext.current
    val imagePicker = rememberFilePickerLauncher(
        type = FilePickerFileType.Image,
        onResult = { images ->
            scope.launch(Dispatchers.IO) {
                val bytes = images.firstOrNull()?.readByteArray(platformContext)
                bytes?.let {
                    LoadingOverlayController.showLoading()
                    onAction(QrScannerAction.QrScannedFromImage(it))
                }
            }
        }
    )

    ShowErrorSnackbar(
        error = state.error?.asString(),
        snackbarHostState = snackbarHostState,
        onDismiss = { onAction(QrScannerAction.DismissError) }
    )

    Scaffold(
        topBar = {
            QrScannerTopAppBar(
                onNavigateBack = { onAction(QrScannerAction.NavigateBack) },
                onToggleFlashlight = { onAction(QrScannerAction.ToggleFlashlight) },
                onOpenImagePicker = imagePicker::launch,
                isFlashlightOn = state.flashlightOn
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier
            .fillMaxSize()
            .sharedTransition {
                Modifier.sharedBounds(
                    sharedContentState = rememberSharedContentState(
                        key = FAB_EXPLODE_BOUNDS_KEY
                    ),
                    animatedVisibilityScope = it,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                )
            }
    ) {
        QrScannerBox(
            state = state,
            onAction = onAction,
            errorMessage = errorMessage
        )
    }
}

@Composable
private fun QrScannerTopAppBar(
    onNavigateBack: () -> Unit,
    onToggleFlashlight: () -> Unit,
    onOpenImagePicker: () -> Unit,
    isFlashlightOn: Boolean
) {
    TopAppBar(
        title = { Text(stringResource(Res.string.scan_qr_code)) },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Rounded.Close, "close")
            }
        },
        actions = {
            IconButton(onClick = onToggleFlashlight) {
                Icon(
                    painter = painterResource(
                        if (isFlashlightOn) {
                            Res.drawable.ic_flash_on
                        } else {
                            Res.drawable.ic_flash_off
                        }
                    ),
                    contentDescription = "Flashlight"
                )
            }
            IconButton(onClick = onOpenImagePicker) {
                Icon(
                    painter = painterResource(Res.drawable.ic_gallery),
                    contentDescription = "Gallery"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            actionIconContentColor = MaterialTheme.colors.iconColor,
            navigationIconContentColor = MaterialTheme.colors.iconColor,
            titleContentColor = MaterialTheme.colors.textColor
        )
    )
}

@Composable
private fun QrScannerBox(
    state: QrScannerState,
    onAction: (QrScannerAction) -> Unit,
    errorMessage: String,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    Box(modifier = modifier.fillMaxSize()) {
        QrScanner(
            modifier = Modifier,
            flashlightOn = state.flashlightOn,
            openImagePicker = false,
            onCompletion = { onAction(QrScannerAction.OnQrCodeScanned(it)) },
            imagePickerHandler = {},
            cameraLens = CameraLens.Back,
            onFailure = { error ->
                scope.launch {
                    snackBarHostState.showSnackbar(error.ifEmpty { errorMessage })
                }
            }
        )

        QrScannerBottomContent(
            onNavigateToEnterKeyManually = { onAction(QrScannerAction.EnterKeyManually) },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun QrScannerBottomContent(
    onNavigateToEnterKeyManually: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color.Black.copy(0.3f))
            .navigationBarsPadding()
    ) {
        Text(
            text = stringResource(Res.string.point_camera_at_qr_code),
            color = Color.White
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(Res.string.cant_scan_qr_code),
                color = Color.White
            )
            TextButton(
                onClick = onNavigateToEnterKeyManually,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = LightOptCodeFinishingColor
                )
            ) {
                Text(stringResource(Res.string.enter_key_manually))
            }
        }
    }
}
