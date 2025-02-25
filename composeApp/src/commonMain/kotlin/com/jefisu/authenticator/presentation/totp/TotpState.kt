package com.jefisu.authenticator.presentation.totp

import arrow.optics.optics
import com.jefisu.authenticator.core.presentation.util.UiText
import com.jefisu.authenticator.domain.model.TwoFactorAuthAccount

@optics
data class TotpState(
    val totpCodes: List<TotpCode> = emptyList(),
    val error: UiText? = null,
    val searchQuery: String = "",
    val isSearching: Boolean = false,
    val searchResults: List<TotpCode> = emptyList()
) {
    companion object
}

@optics
data class TotpCode(
    val account: TwoFactorAuthAccount,
    val code: String,
    val remainingTime: Int
) {
    companion object
}
