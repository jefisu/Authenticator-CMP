package com.jefisu.authenticator.domain.repository

import com.jefisu.authenticator.domain.model.Account
import com.jefisu.authenticator.domain.util.DataError
import com.jefisu.authenticator.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    suspend fun addAccount(account: Account): Result<Unit, DataError>

    suspend fun deleteAccount(account: Account): Result<Unit, DataError>

    fun getAllAccounts(): Flow<List<Account>>
}
