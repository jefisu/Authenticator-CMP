package com.jefisu.authenticator.presentation.totp

sealed interface TotpAction {
    data object NavigateQrScanner : TotpAction
    data object NavigateEnterKeyManually : TotpAction
    data class QrScannedFromImage(val totpUri: String) : TotpAction
}
