package com.jefisu.authenticator.data

import com.jefisu.authenticator.domain.model.TwoFactorAuthAccount
import com.jefisu.authenticator.domain.repository.AccountRepository
import com.jefisu.authenticator.domain.util.Error
import com.jefisu.authenticator.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeAccountRepository : AccountRepository {

    private val accounts = mutableListOf<TwoFactorAuthAccount>()
    private var shouldFail = false

    override suspend fun addAccount(account: TwoFactorAuthAccount): Result<Unit, Error> {
        checkFailure()?.let { return it }

        return accounts.add(account).let { Result.Success(Unit) }
    }

    override suspend fun deleteAccount(account: TwoFactorAuthAccount): Result<Unit, Error> {
        checkFailure()?.let { return it }

        return accounts.remove(account).let { Result.Success(Unit) }
    }

    override fun getAllAccounts(): Flow<List<TwoFactorAuthAccount>> {
        return flowOf(accounts)
    }

    fun forceNextOperationFailure() {
        shouldFail = true
    }

    private fun checkFailure(): Result<Nothing, Error>? {
        if (!shouldFail) return null

        shouldFail = false
        return Result.Error(Error.Unknown)
    }
}
