package com.jefisu.authenticator.domain.validation

import com.jefisu.authenticator.domain.util.Error

data class ValidationResult(
    val sucessfully: Boolean,
    val error: Error? = null
)
