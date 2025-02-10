package com.jefisu.authenticator.presentation.qrscanner

sealed interface QrScannerAction {
    data class OnQrCodeScanned(val content: String) : QrScannerAction
    data object ToggleFlashlight : QrScannerAction
    data object EnterKeyManually : QrScannerAction
    data object NavigateBack : QrScannerAction
    data object DismissError : QrScannerAction
}
