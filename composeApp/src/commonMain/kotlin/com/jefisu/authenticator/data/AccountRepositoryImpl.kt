package com.jefisu.authenticator.data

import com.jefisu.authenticator.data.database.AccountDatabase
import com.jefisu.authenticator.data.database.AccountEntity
import com.jefisu.authenticator.data.database.toAccount
import com.jefisu.authenticator.data.database.toAccountEntity
import com.jefisu.authenticator.data.util.runSafelyResult
import com.jefisu.authenticator.domain.model.TwoFactorAuthAccount
import com.jefisu.authenticator.domain.repository.AccountRepository
import com.jefisu.authenticator.domain.util.Error
import com.jefisu.authenticator.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AccountRepositoryImpl(
    private val db: AccountDatabase
) : AccountRepository {

    override suspend fun addAccount(account: TwoFactorAuthAccount): Result<Unit, Error> {
        return runSafelyResult {
            db.accountDao.upsert(account.encryptEntity())
        }
    }

    override suspend fun deleteAccount(account: TwoFactorAuthAccount): Result<Unit, Error> {
        return runSafelyResult {
            db.accountDao.delete(account.encryptEntity())
        }
    }

    override fun getAllAccounts(): Flow<List<TwoFactorAuthAccount>> {
        return db.accountDao.getAll().map { accounts ->
            accounts.mapNotNull {
                runCatching {
                    val decryptedJson = CryptoManager.decrypt(it.encryptedBase64)
                    it.toAccount(decryptedJson)
                }.getOrNull()
            }
        }
    }

    private suspend fun TwoFactorAuthAccount.encryptEntity(): AccountEntity {
        val encryptedJson = Json.encodeToString(this)
        val encryptedBase64 = CryptoManager.encrypt(encryptedJson)
        return toAccountEntity(encryptedBase64)
    }
}
