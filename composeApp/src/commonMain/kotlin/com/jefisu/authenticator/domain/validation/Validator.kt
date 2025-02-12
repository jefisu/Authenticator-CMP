package com.jefisu.authenticator.domain.validation

fun interface Validator<D> {
    fun validate(data: D): ValidationResult
}