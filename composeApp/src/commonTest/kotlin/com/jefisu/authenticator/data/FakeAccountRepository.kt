package com.jefisu.authenticator.data

import com.jefisu.authenticator.domain.model.TwoFactorAuthAccount
import com.jefisu.authenticator.domain.repository.AccountRepository
import com.jefisu.authenticator.domain.util.Error
import com.jefisu.authenticator.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeAccountRepository : AccountRepository {

    private var shouldFail = false
    private val accountFlow = MutableStateFlow(emptyList<TwoFactorAuthAccount>())
    private var accounts = emptyList<TwoFactorAuthAccount>()
        set(value) {
            field = value
            accountFlow.update { value }
        }

    override suspend fun addAccount(account: TwoFactorAuthAccount): Result<Unit, Error> {
        checkFailure()?.let { return it }

        accounts += account
        return Result.Success(Unit)
    }

    override suspend fun deleteAccount(account: TwoFactorAuthAccount): Result<Unit, Error> {
        checkFailure()?.let { return it }

        accounts -= account
        return Result.Success(Unit)
    }

    override fun getAllAccounts(): Flow<List<TwoFactorAuthAccount>> {
        return accountFlow
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
