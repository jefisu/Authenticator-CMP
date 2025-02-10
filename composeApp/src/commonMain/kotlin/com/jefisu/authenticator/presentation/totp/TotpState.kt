package com.jefisu.authenticator.presentation.totp

import arrow.optics.optics
import com.jefisu.authenticator.core.presentation.util.UiText
import com.jefisu.authenticator.core.util.TotpConstants
import com.jefisu.authenticator.domain.model.Account

@optics
data class TotpState(
    val totpAccounts: Map<String, Account> = emptyMap(),
    val timeUntilTotpRefresh: Int = TotpConstants.REFRESH_INTERVAL,
    val error: UiText? = null
) {
    companion object
}
