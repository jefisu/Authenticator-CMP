package com.jefisu.authenticator.domain.usecase

import com.jefisu.authenticator.domain.model.TwoFactorAuthAccount
import com.jefisu.authenticator.domain.repository.AccountRepository
import com.jefisu.authenticator.domain.util.Error
import com.jefisu.authenticator.domain.util.Result

class DeleteAccountUseCase(
    private val repository: AccountRepository
) {
    suspend fun execute(account: TwoFactorAuthAccount): Result<Unit, Error> {
        return repository.deleteAccount(account)
    }
}
