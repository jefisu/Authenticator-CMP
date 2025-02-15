package com.jefisu.authenticator.presentation.qrscanner

sealed interface QrScannerAction {
    data class OnQrCodeScanned(val uri: String) : QrScannerAction
    data class QrScannedFromImage(val bytes: ByteArray?) : QrScannerAction
    data object ToggleFlashlight : QrScannerAction
    data object EnterKeyManually : QrScannerAction
    data object NavigateBack : QrScannerAction
    data object DismissError : QrScannerAction
}
