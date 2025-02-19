package com.jefisu.authenticator.domain.usecase

import com.jefisu.authenticator.domain.model.TwoFactorAuthAccount
import com.jefisu.authenticator.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class SearchAccountsUseCase(
    private val repository: AccountRepository
) {
    fun execute(query: String): Flow<List<TwoFactorAuthAccount>> {
        return repository
            .getAllAccounts()
            .map { accounts ->
                accounts.filter { it.containsQuery(query) }
            }
            .takeIf { query.isNotEmpty() } ?: flowOf(emptyList())
    }

    private fun TwoFactorAuthAccount.containsQuery(query: String): Boolean {
        return name.containsIgnoreCase(query) ||
                login.containsIgnoreCase(query) ||
                issuer?.identifier?.containsIgnoreCase(query) ?: false
    }

    private fun String.containsIgnoreCase(other: String): Boolean {
        return this.lowercase().contains(other.lowercase())
    }
}