package com.jefisu.authenticator.data

import com.jefisu.authenticator.domain.model.Account
import com.jefisu.authenticator.domain.service.TotpService
import diglol.crypto.Hmac
import diglol.crypto.otp.Totp

class TotpServiceImpl : TotpService {

    private fun getTotpGenerator(account: Account): Totp {
        return Totp(
            hmacType = account.algorithm,
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
