package com.jefisu.authenticator.domain.usecase

import com.jefisu.authenticator.domain.model.TwoFactorAuthAccount
import com.jefisu.authenticator.domain.service.TotpService

class GenerateTotpUseCase(
    private val service: TotpService
) {
    suspend fun execute(account: TwoFactorAuthAccount): String {
        return service.generateTotpCode(account)
    }
}
