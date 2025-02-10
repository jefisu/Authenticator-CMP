package com.jefisu.authenticator.domain.usecase

import com.jefisu.authenticator.domain.model.Account
import com.jefisu.authenticator.domain.repository.AccountRepository
import com.jefisu.authenticator.domain.util.DataError
import com.jefisu.authenticator.domain.util.Result
import com.jefisu.authenticator.domain.util.parseTotpUri

class AddAccountUseCase(
    private val repository: AccountRepository
) {
    suspend fun execute(totpUri: String): Result<Unit, DataError> {
        val account = parseTotpUri(totpUri)
            ?: return Result.Error(DataError.Account.INVALID_QR_CODE)

        return repository.addAccount(account)
    }

    suspend fun execute(account: Account): Result<Unit, DataError> {
        return repository.addAccount(account)
    }
}
