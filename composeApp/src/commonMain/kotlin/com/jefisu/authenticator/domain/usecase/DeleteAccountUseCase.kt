package com.jefisu.authenticator.domain.usecase

import com.jefisu.authenticator.domain.model.Account
import com.jefisu.authenticator.domain.repository.AccountRepository
import com.jefisu.authenticator.domain.util.DataError
import com.jefisu.authenticator.domain.util.Result

class DeleteAccountUseCase(
    private val repository: AccountRepository
) {
    suspend fun execute(account: Account): Result<Unit, DataError> {
        return repository.deleteAccount(account)
    }
}