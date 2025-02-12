package com.jefisu.authenticator.domain.repository

import com.jefisu.authenticator.domain.model.Account
import com.jefisu.authenticator.domain.util.Error
import com.jefisu.authenticator.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    suspend fun addAccount(account: Account): Result<Unit, Error>

    suspend fun deleteAccount(account: Account): Result<Unit, Error>

    fun getAllAccounts(): Flow<List<Account>>
}
