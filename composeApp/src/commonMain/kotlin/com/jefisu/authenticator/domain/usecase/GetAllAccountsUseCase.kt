package com.jefisu.authenticator.domain.usecase

import com.jefisu.authenticator.domain.model.TwoFactorAuthAccount
import com.jefisu.authenticator.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

class GetAllAccountsUseCase(
    private val repository: AccountRepository
) {

    fun execute(): Flow<List<TwoFactorAuthAccount>> {
        return repository.getAllAccounts()
    }
}
