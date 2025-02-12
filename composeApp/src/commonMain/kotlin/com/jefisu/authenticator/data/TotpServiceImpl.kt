package com.jefisu.authenticator.data

import com.jefisu.authenticator.domain.model.Account
import com.jefisu.authenticator.domain.model.Algorithm
import com.jefisu.authenticator.domain.service.TotpService
import diglol.crypto.Hmac
import diglol.crypto.otp.Totp

class TotpServiceImpl : TotpService {

    private fun Account.hmacAlgorithm(): Hmac.Type = when (algorithm) {
        Algorithm.SHA1 -> Hmac.Type.SHA1
        Algorithm.SHA256 -> Hmac.Type.SHA256
        Algorithm.SHA384 -> Hmac.Type.SHA384
        Algorithm.SHA512 -> Hmac.Type.SHA512
    }

    private fun getTotpGenerator(account: Account): Totp {
        return Totp(
            hmacType = account.hmacAlgorithm(),
            hmacKey = account.secret.encodeToByteArray(),
            period = account.refreshPeriod,
            codeLength = account.digitCount,
            issuer = account.issuer?.identifier.orEmpty(),
            accountName = account.login
        )
    }

    override suspend fun generateTotpCode(account: Account): String {
        val totpGenerator = getTotpGenerator(account)
        return totpGenerator.generate()
    }
}
