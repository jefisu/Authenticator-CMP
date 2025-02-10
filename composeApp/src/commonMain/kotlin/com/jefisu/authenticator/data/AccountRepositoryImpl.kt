package com.jefisu.authenticator.data

import com.jefisu.authenticator.data.util.runSafelyResult
import com.jefisu.authenticator.domain.model.Account
import com.jefisu.authenticator.domain.repository.AccountRepository
import com.jefisu.authenticator.domain.util.DataError
import com.jefisu.authenticator.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class AccountRepositoryImpl : AccountRepository {

    override suspend fun addAccount(account: Account): Result<Unit, DataError> {
        return runSafelyResult { }
    }

    override suspend fun deleteAccount(account: Account): Result<Unit, DataError> {
        return runSafelyResult { }
    }

    override fun getAllAccounts(): Flow<List<Account>> {
        return emptyFlow()
    }
}
