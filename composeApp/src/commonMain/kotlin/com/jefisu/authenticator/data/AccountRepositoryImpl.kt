package com.jefisu.authenticator.data

import com.jefisu.authenticator.data.database.AccountDatabase
import com.jefisu.authenticator.data.database.AccountEntity
import com.jefisu.authenticator.data.database.toAccount
import com.jefisu.authenticator.data.database.toAccountEntity
import com.jefisu.authenticator.data.util.runSafelyResult
import com.jefisu.authenticator.domain.model.Account
import com.jefisu.authenticator.domain.repository.AccountRepository
import com.jefisu.authenticator.domain.util.Error
import com.jefisu.authenticator.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AccountRepositoryImpl(
    private val db: AccountDatabase
) : AccountRepository {

    override suspend fun addAccount(account: Account): Result<Unit, Error> {
        return runSafelyResult {
            db.accountDao.upsert(account.toAccountEntity())
        }
    }

    override suspend fun deleteAccount(account: Account): Result<Unit, Error> {
        return runSafelyResult {
            db.accountDao.delete(account.toAccountEntity())
        }
    }

    override fun getAllAccounts(): Flow<List<Account>> {
        return db.accountDao
            .getAll()
            .map { it.map(AccountEntity::toAccount) }
    }
}
