package com.jefisu.authenticator.domain.validation

import com.jefisu.authenticator.domain.util.Error

data class ValidationRule<T>(
    val validate: (T) -> Boolean,
    val error: Error
)
