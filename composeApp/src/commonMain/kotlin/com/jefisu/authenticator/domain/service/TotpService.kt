package com.jefisu.authenticator.domain.service

import com.jefisu.authenticator.domain.model.Account

interface TotpService {
    suspend fun generateTotpCode(account: Account): String
}
