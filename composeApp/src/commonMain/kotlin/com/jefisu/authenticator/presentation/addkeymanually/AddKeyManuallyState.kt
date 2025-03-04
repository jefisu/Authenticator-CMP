package com.jefisu.authenticator.presentation.addkeymanually

import arrow.optics.optics
import com.jefisu.authenticator.core.presentation.util.UiText
import com.jefisu.authenticator.core.util.TotpConstants
import com.jefisu.authenticator.domain.model.Algorithm
import com.jefisu.authenticator.domain.model.TwoFactorAuthAccount

@optics
data class AddKeyManuallyState(
    val account: TwoFactorAuthAccount = emptyAccount,
    val unsavedChanges: Boolean = false,
    val settingsExpanded: Boolean = false,
    val searchService: String = "",
    val error: UiText? = null,
    val saved: Boolean = false,
    val dataLoaded: Boolean = false,
    val isEditMode: Boolean = false
) {
    companion object
}

private val emptyAccount = TwoFactorAuthAccount(
    name = "",
    login = "",
    secret = "",
    issuer = null,
    refreshPeriod = TotpConstants.REFRESH_INTERVAL,
    digitCount = TotpConstants.CODE_LENGTH,
    algorithm = Algorithm.SHA1
)
