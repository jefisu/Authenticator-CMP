package com.jefisu.authenticator.presentation.addkeymanually

import arrow.optics.optics
import com.jefisu.authenticator.core.presentation.util.UiText
import com.jefisu.authenticator.domain.model.Issuer
import diglol.crypto.Hmac

@optics
data class AddKeyManuallyState(
    val accountName: String = "",
    val login: String = "",
    val secretKey: String = "",
    val issuer: Issuer? = null,
    val unsavedChanges: Boolean = false,
    val settingsExpanded: Boolean = false,
    val searchService: String = "",
    val algorithm: Hmac.Type = Hmac.Type.SHA1,
    val refreshPeriod: Int = 30,
    val digits: Int = 6,
    val error: UiText? = null,
    val saved: Boolean = false
) {
    companion object
}
