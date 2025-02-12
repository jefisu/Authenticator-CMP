package com.jefisu.authenticator.domain.usecase

import com.jefisu.authenticator.domain.model.Account
import com.jefisu.authenticator.domain.repository.AccountRepository
import com.jefisu.authenticator.domain.util.Error
import com.jefisu.authenticator.domain.util.Result
import com.jefisu.authenticator.domain.util.parseTotpUri
import com.jefisu.authenticator.domain.validation.AccountValidationError
import com.jefisu.authenticator.domain.validation.accountRulesValidator

class AddAccountUseCase(
    private val repository: AccountRepository
) {
    suspend fun execute(totpUri: String): Result<Unit, Error> {
        val account = parseTotpUri(totpUri)
            ?: return Result.Error(AccountValidationError.INVALID_QR_CODE)

        return repository.addAccount(account)
    }

    suspend fun execute(account: Account): Result<Unit, Error> {
        val validationResult = accountRulesValidator.validate(account)
        if (!validationResult.sucessfully) {
            return Result.Error(validationResult.error!!)
        }

        return repository.addAccount(account)
    }
}