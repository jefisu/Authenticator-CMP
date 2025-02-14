package com.jefisu.authenticator.domain.service

import com.jefisu.authenticator.domain.model.TwoFactorAuthAccount

interface TotpService {
    suspend fun generateTotpCode(account: TwoFactorAuthAccount): String
}
