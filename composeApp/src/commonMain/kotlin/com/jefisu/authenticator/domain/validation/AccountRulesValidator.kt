package com.jefisu.authenticator.domain.validation

import com.jefisu.authenticator.domain.model.TwoFactorAuthAccount
import com.jefisu.authenticator.domain.util.Error

val accountRulesValidator = Validator<TwoFactorAuthAccount> { account ->
    validationRules
        .firstOrNull { !it.validate(account) }
        ?.let { rule -> ValidationResult(sucessfully = false, error = rule.error) }
        ?: ValidationResult(sucessfully = true)
}

enum class AccountValidationError : Error {
    INVALID_QR_CODE,
    INVALID_LOGIN,
    INVALID_SECRET
}

private val validationRules = listOf(
    ValidationRule<TwoFactorAuthAccount>(
        validate = { it.login.isNotEmpty() },
        error = AccountValidationError.INVALID_LOGIN
    ),
    ValidationRule(
        validate = { it.secret.length == it.algorithm.length },
        error = AccountValidationError.INVALID_SECRET
    )
)
