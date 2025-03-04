package com.jefisu.authenticator.data

import com.jefisu.authenticator.domain.model.TwoFactorAuthAccount
import com.jefisu.authenticator.domain.service.TotpService

class FakeTotpService : TotpService {

    private var nextCode = "043143"
    private var shouldFail = false

    fun setNextCode(code: String) {
        nextCode = code
    }

    fun setNextCallToFail() {
        shouldFail = true
    }

    override suspend fun generateTotpCode(account: TwoFactorAuthAccount): String {
        if (shouldFail) {
            shouldFail = false
            throw IllegalArgumentException("Failed to generate TOTP code")
        }

        return nextCode
    }
}
