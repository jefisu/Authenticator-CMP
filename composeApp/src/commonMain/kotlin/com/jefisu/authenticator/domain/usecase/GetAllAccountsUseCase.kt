package com.jefisu.authenticator.domain.usecase

import com.jefisu.authenticator.domain.model.Account
import com.jefisu.authenticator.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

class GetAllAccountsUseCase(
    private val repository: AccountRepository
) {

    fun execute(): Flow<List<Account>> {
        return repository.getAllAccounts()
    }
}
