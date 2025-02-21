package com.jefisu.authenticator.domain.util

import com.jefisu.authenticator.core.util.TotpConstants.CODE_LENGTH
import com.jefisu.authenticator.core.util.TotpConstants.REFRESH_INTERVAL
import com.jefisu.authenticator.domain.model.Algorithm
import com.jefisu.authenticator.domain.model.Issuer
import com.jefisu.authenticator.domain.model.TwoFactorAuthAccount

fun parseTotpUri(uri: String): TwoFactorAuthAccount? {
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
    val accountOrigin = issuerRegex.find(uri)?.groupValues?.get(1) ?: return null
    val email = finalEmail ?: return null
    val digits = digitsRegex.find(uri)?.groupValues?.get(1)?.toIntOrNull() ?: CODE_LENGTH
    val period = periodRegex.find(uri)?.groupValues?.get(1)?.toIntOrNull() ?: REFRESH_INTERVAL

    val issuer = Issuer.findByIdentifier(accountOrigin)
    return TwoFactorAuthAccount(
        name = issuer?.identifier.orEmpty(),
        issuer = issuer,
        login = email,
        secret = secret,
        refreshPeriod = period,
        digitCount = digits,
        algorithm = Algorithm.SHA1
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
