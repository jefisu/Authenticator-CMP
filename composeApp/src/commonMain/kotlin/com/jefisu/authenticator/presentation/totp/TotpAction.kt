package com.jefisu.authenticator.presentation.totp

import com.jefisu.authenticator.domain.model.TwoFactorAuthAccount

sealed interface TotpAction {
    data object NavigateQrScanner : TotpAction
    data class NavigateEnterKeyManually(val accountId: Int?) : TotpAction
    data class QrScannedFromImage(val totpUri: String) : TotpAction
    data class DeleteAccount(val account: TwoFactorAuthAccount) : TotpAction
}
