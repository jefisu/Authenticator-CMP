package com.jefisu.authenticator.domain.usecase

import com.jefisu.authenticator.domain.model.Account
import com.jefisu.authenticator.domain.service.TotpService

class GenerateTotpUseCase(
    private val service: TotpService
) {
    suspend fun execute(account: Account): String {
        return service.generateTotpCode(account)
    }
}
