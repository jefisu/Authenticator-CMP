package com.jefisu.authenticator.domain.repository

import com.jefisu.authenticator.domain.model.TwoFactorAuthAccount
import com.jefisu.authenticator.domain.util.Error
import com.jefisu.authenticator.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    suspend fun addAccount(account: TwoFactorAuthAccount): Result<Unit, Error>

    suspend fun deleteAccount(account: TwoFactorAuthAccount): Result<Unit, Error>

    fun getAllAccounts(): Flow<List<TwoFactorAuthAccount>>
}
