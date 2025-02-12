package com.jefisu.authenticator.presentation.totp

import com.jefisu.authenticator.domain.model.Account

sealed interface TotpAction {
    data object NavigateQrScanner : TotpAction
    data class NavigateEnterKeyManually(val accountId: Int?) : TotpAction
    data class QrScannedFromImage(val totpUri: String) : TotpAction
    data class DeleteAccount(val account: Account) : TotpAction
}
