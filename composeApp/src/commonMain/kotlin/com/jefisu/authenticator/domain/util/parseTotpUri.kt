package com.jefisu.authenticator.domain.util

import com.jefisu.authenticator.core.util.TotpConstants.CODE_LENGTH
import com.jefisu.authenticator.core.util.TotpConstants.REFRESH_INTERVAL
import com.jefisu.authenticator.domain.model.Account
import com.jefisu.authenticator.domain.model.CustomIssuer

fun parseTotpUri(uri: String): Account? {
    val emailRegex = "totp/(?:.*?:)?([^?]+)".toRegex()
    val issuerRegex = "issuer=([^&]+)".toRegex()
    val secretRegex = "secret=([^&]+)".toRegex()
    val digitsRegex = "digits=(\\d+)".toRegex()
    val periodRegex = "period=(\\d+)".toRegex()

    val rawEmail = emailRegex.find(uri)?.groupValues?.get(1)
    val decodedEmail = rawEmail?.let { decodeUrl(it) }

    val finalEmail = decodedEmail?.let {
        if (it.contains(":")) it.split(":")[1] else it
    }

    val secret = secretRegex.find(uri)?.groupValues?.get(1) ?: return null
    val issuer = issuerRegex.find(uri)?.groupValues?.get(1) ?: return null
    val email = finalEmail ?: return null
    val digits = digitsRegex.find(uri)?.groupValues?.get(1)?.toIntOrNull() ?: CODE_LENGTH
    val period = periodRegex.find(uri)?.groupValues?.get(1)?.toIntOrNull() ?: REFRESH_INTERVAL
    return Account(
        name = "",
        issuer = DefaultIssuer.entries
            .firstOrNull { it.identifier.contains(issuer, ignoreCase = true) }
            ?: CustomIssuer(issuer, ""),
        login = email,
        secret = secret,
        id = 0,
        refreshPeriod = period,
        digitCount = digits
    )
}

private fun decodeUrl(encoded: String): String {
    return encoded.replace(
        regex = "%[0-9A-Fa-f]{2}".toRegex()
    ) { matchResult ->
        val hex = matchResult.value.substring(1)
        Char(hex.toInt(16)).toString()
    }
}
